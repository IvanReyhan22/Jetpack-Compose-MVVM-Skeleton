package id.codemockup.ramu

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.test.ext.junit.runners.AndroidJUnit4
import id.codemockup.ramu.core.common.DataState
import id.codemockup.ramu.core.model.chat.ChatConversation
import id.codemockup.ramu.designsystem.theme.RamuTheme
import id.codemockup.ramu.feature.chat.ChatContent
import id.codemockup.ramu.feature.chat.ChatState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatMenuAndRefreshTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

    private val state = ChatState(conversation = DataState(data = ChatConversation("id", emptyList())))

    @Test fun menuShowsSessionActionsAndInvokesCallbacks() {
        var created = 0
        var deleted = 0
        compose.setContent {
            RamuTheme { ChatContent(state, {}, {}, {}, onNewSession = { created++ }, onDeleteSession = { deleted++ }) }
        }
        compose.onNodeWithContentDescription("More options").performClick()
        compose.onNodeWithText("New Session").assertIsDisplayed()
        compose.onNodeWithText("Delete Session").assertIsDisplayed()
        compose.onNodeWithText("New Session").performClick()
        compose.runOnIdle { assertEquals(1, created) }
        compose.onNodeWithContentDescription("More options").performClick()
        compose.onNodeWithText("Delete Session").performClick()
        compose.runOnIdle { assertEquals(1, deleted) }
    }

    @Test fun pullDownRefreshes() {
        var refreshes = 0
        compose.setContent { RamuTheme { ChatContent(state, {}, {}, { refreshes++ }) } }
        compose.onNodeWithTag("chat_messages").performTouchInput { swipeDown(durationMillis = 400) }
        compose.waitUntil(5_000) { refreshes == 1 }
    }
}
