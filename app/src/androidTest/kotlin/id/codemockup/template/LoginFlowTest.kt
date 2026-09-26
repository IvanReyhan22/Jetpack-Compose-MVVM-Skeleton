package id.codemockup.template

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginFlowTest {
    @get:Rule val compose = createEmptyComposeRule()

    private fun waitFor(text: String) {
        compose.waitUntil(10_000) {
            compose.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test fun loginValidationErrorsSessionRestoreLogoutAndBackStack() {
        var scenario = ActivityScenario.launch(MainActivity::class.java)
        try {
            compose.waitUntil(10_000) {
                compose.onAllNodesWithText("Sign in").fetchSemanticsNodes().isNotEmpty() ||
                    compose.onAllNodesWithText("Sign out").fetchSemanticsNodes().isNotEmpty()
            }
            if (compose.onAllNodesWithText("Sign out").fetchSemanticsNodes().isNotEmpty()) {
                compose.onNodeWithText("Sign out").performClick()
            }
            waitFor("Sign in")
            compose.onNodeWithText("Sign in").performClick()
            compose.onNodeWithText("Enter your email.").assertIsDisplayed()
            compose.onNodeWithText("Enter your password.").assertIsDisplayed()
            compose.onNodeWithText("Email").performTextInput("demo@example.com")
            compose.onNodeWithText("Password").performTextInput("wrong")
            compose.onNodeWithText("Sign in").performClick()
            waitFor("Invalid email or password.")
            compose.onNodeWithText("Password").performTextReplacement("password123")
            compose.onNodeWithText("Sign in").performClick()
            waitFor("Signed in")
            compose.onNodeWithText("demo@example.com").assertIsDisplayed()

            // Back exits instead of exposing the login screen behind an active session.
            scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }
            compose.waitUntil(5_000) { scenario.state == androidx.lifecycle.Lifecycle.State.DESTROYED }
            scenario.close()
            scenario = ActivityScenario.launch(MainActivity::class.java)
            waitFor("Signed in")
            scenario.recreate()
            waitFor("Signed in")
            compose.onNodeWithText("Sign out").performClick()
            waitFor("Sign in")
            scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }
            compose.waitUntil(5_000) { scenario.state == androidx.lifecycle.Lifecycle.State.DESTROYED }
            scenario.close()
            scenario = ActivityScenario.launch(MainActivity::class.java)
            waitFor("Sign in")
            assertTrue(compose.onAllNodesWithText("Signed in").fetchSemanticsNodes().isEmpty())
        } finally { scenario.close() }
    }
}
