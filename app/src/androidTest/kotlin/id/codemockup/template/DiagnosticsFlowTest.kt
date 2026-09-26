package id.codemockup.template

import android.content.pm.PackageManager
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.EntryPointAccessors
import id.codemockup.template.core.common.ErrorMessageConstant
import id.codemockup.template.core.common.utils.sentry.SentryService
import id.codemockup.template.core.model.session.User
import id.codemockup.template.core.model.session.UserSession
import id.codemockup.template.diagnostics.DiagnosticsEntryPoint
import io.sentry.Sentry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.net.SocketTimeoutException

@RunWith(AndroidJUnit4::class)
class DiagnosticsFlowTest {
    @get:Rule val compose = createEmptyComposeRule()
    private val context get() = InstrumentationRegistry.getInstrumentation().targetContext
    private val dependencies get() = EntryPointAccessors.fromApplication(context, DiagnosticsEntryPoint::class.java)

    private fun waitFor(text: String) {
        compose.waitUntil(10_000) { compose.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty() }
    }

    @Test fun sentryStaysDisabledAndGlobalErrorsAndExpirySurviveBackgrounding() {
        val wiring = dependencies
        assertFalse(BuildConfig.SENTRY_ENABLED)
        assertEquals("", BuildConfig.SENTRY_DSN)
        assertFalse(SentryService.isEnabled)
        assertFalse(Sentry.isEnabled())
        @Suppress("DEPRECATION")
        val metadata = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA).metaData
        assertFalse(metadata.getBoolean("io.sentry.auto-init", true))
        wiring.sessions().reset()
        wiring.networkErrors().events.value?.let(wiring.networkErrors()::acknowledge)
        runBlocking { wiring.dataStore().saveSession(UserSession("test-token", User("test", "demo@example.com"))) }
        var scenario = ActivityScenario.launch(MainActivity::class.java)
        try {
            waitFor("Signed in")
            scenario.moveToState(Lifecycle.State.CREATED)
            val offline = wiring.httpClient().newBuilder().addInterceptor { throw SocketTimeoutException() }.build()
            try {
                offline.newCall(Request.Builder().url("https://example.com/test").build()).execute().close()
                fail("Expected timeout")
            } catch (_: IOException) { }
            assertNotNull(wiring.networkErrors().events.value)
            scenario.moveToState(Lifecycle.State.RESUMED)
            waitFor("Connection problem")
            compose.onNodeWithText(ErrorMessageConstant.REQUEST_TIMEOUT).assertIsDisplayed()
            compose.onNodeWithText("OK").performClick()
            compose.waitUntil(5_000) { wiring.networkErrors().events.value == null }

            scenario.moveToState(Lifecycle.State.CREATED)
            val unauthorized = wiring.httpClient().newBuilder().addInterceptor { chain ->
                Response.Builder().request(chain.request()).protocol(Protocol.HTTP_1_1)
                    .code(401).message("Unauthorized").body("{}".toResponseBody()).build()
            }.build()
            try {
                unauthorized.newCall(Request.Builder().url("https://example.com/protected").header("@", "Auth").build()).execute().close()
                fail("Expected unauthorized")
            } catch (_: IOException) { }
            assertNull(runBlocking { wiring.dataStore().session.first() })
            assertNotNull(wiring.sessions().events.value)
            scenario.moveToState(Lifecycle.State.RESUMED)
            waitFor("Sign in")
            compose.waitUntil(5_000) { wiring.sessions().events.value == null }
            scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }
            compose.waitUntil(5_000) { scenario.state == Lifecycle.State.DESTROYED }
            scenario.close()
            scenario = ActivityScenario.launch(MainActivity::class.java)
            waitFor("Sign in")
            assertTrue(compose.onAllNodesWithText("Signed in").fetchSemanticsNodes().isEmpty())
            assertFalse(Sentry.isEnabled())
        } finally {
            scenario.close()
            runBlocking { wiring.dataStore().clearSession() }
            wiring.sessions().reset()
            wiring.networkErrors().events.value?.let(wiring.networkErrors()::acknowledge)
        }
    }
}
