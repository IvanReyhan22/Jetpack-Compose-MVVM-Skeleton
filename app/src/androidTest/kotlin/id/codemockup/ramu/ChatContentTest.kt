package id.codemockup.ramu

import androidx.activity.ComponentActivity
import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import id.codemockup.ramu.core.common.DataState
import id.codemockup.ramu.core.model.chat.*
import id.codemockup.ramu.feature.chat.*
import id.codemockup.ramu.designsystem.theme.RamuTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatContentTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()
    @Test fun sendRendersBubblesAndWaitingDisablesSend() {
        var state by mutableStateOf(ChatState(conversation = DataState(data = ChatConversation("id", emptyList()))))
        var sends = 0
        compose.setContent { RamuTheme { ChatContent(state, { state = state.copy(draft = it) }, {
            sends++
            state = state.copy(draft = "", sending = true, conversation = DataState(data = ChatConversation("id", listOf(ChatMessage("1", "user", "Hello")))))
        }, {}) } }
        compose.onNodeWithContentDescription("Send").assertIsNotEnabled()
        compose.onNode(hasSetTextAction()).performTextInput("Hello")
        compose.onNodeWithContentDescription("Send").performClick()
        compose.onNodeWithText("Hello").assertIsDisplayed()
        compose.onNodeWithContentDescription("Hermes is thinking…").assertIsDisplayed()
        compose.onNodeWithContentDescription("Send").assertIsNotEnabled()
        compose.runOnIdle {
            assertEquals(1, sends)
            state = state.copy(sending = false, conversation = DataState(data = ChatConversation("id", state.conversation.data!!.messages + ChatMessage("2", "assistant", "Hi there"))))
        }
        compose.onNodeWithText("Hi there").assertIsDisplayed()
    }
    @Test fun errorsStayVisibleAndOfferHistoryRefresh() {
        var refreshes = 0
        compose.setContent { RamuTheme {
            ChatContent(ChatState(conversation = DataState(errorMessage = "Connection failed"), needsRefresh = true), {}, {}, { refreshes++ })
        } }
        compose.onNodeWithText("Connection failed").assertIsDisplayed()
        compose.onNodeWithContentDescription("Send").assertIsNotEnabled()
        compose.onNodeWithText("Refresh history").performClick()
        compose.runOnIdle { assertEquals(1, refreshes) }
    }
}
