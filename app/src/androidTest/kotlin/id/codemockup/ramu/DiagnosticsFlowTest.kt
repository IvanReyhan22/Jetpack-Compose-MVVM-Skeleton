package id.codemockup.ramu

import android.content.pm.PackageManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import id.codemockup.ramu.core.common.utils.sentry.SentryService
import io.sentry.Sentry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DiagnosticsFlowTest {
    @Test fun sentryStaysDisabledForChat() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assertFalse(BuildConfig.SENTRY_ENABLED)
        assertEquals("", BuildConfig.SENTRY_DSN)
        assertFalse(SentryService.isEnabled)
        assertFalse(Sentry.isEnabled())
        @Suppress("DEPRECATION")
        val metadata = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA).metaData
        assertFalse(metadata.getBoolean("io.sentry.auto-init", true))
    }
}
