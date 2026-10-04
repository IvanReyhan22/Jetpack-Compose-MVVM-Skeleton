package id.codemockup.ramu

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatFlowTest {
    @get:Rule val compose = createEmptyComposeRule()
    @Test fun chatOpensDirectlyAndKeepsDraftAcrossActivityRecreation() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            compose.waitUntil(10_000) { compose.onAllNodesWithText("Hermes").fetchSemanticsNodes().isNotEmpty() }
            compose.onNodeWithText("Hermes").assertIsDisplayed()
            compose.onNodeWithText("Sign in").assertDoesNotExist()
            compose.onNode(hasSetTextAction()).performTextInput("Next question")
            scenario.recreate()
            compose.onNode(hasSetTextAction()).assertTextContains("Next question")
        }
    }
}
