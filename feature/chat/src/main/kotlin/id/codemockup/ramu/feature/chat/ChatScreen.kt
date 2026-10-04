package id.codemockup.ramu.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.codemockup.ramu.core.common.DataState
import id.codemockup.ramu.core.model.chat.ChatConversation
import id.codemockup.ramu.core.model.chat.ChatMessage
import id.codemockup.ramu.designsystem.common.enums.AppAlertVariant
import id.codemockup.ramu.designsystem.common.enums.AppMascotMood
import id.codemockup.ramu.designsystem.components.backgrounds.appTopGlow
import id.codemockup.ramu.designsystem.components.feedback.AppInlineAlert
import id.codemockup.ramu.designsystem.components.feedback.AppPullToRefresh
import id.codemockup.ramu.designsystem.components.icons.AppMascot
import id.codemockup.ramu.designsystem.components.navigation.AppBar
import id.codemockup.ramu.designsystem.theme.AppColors
import id.codemockup.ramu.designsystem.theme.AppControlTokens
import id.codemockup.ramu.designsystem.theme.AppRadius
import id.codemockup.ramu.designsystem.theme.AppSpacing
import id.codemockup.ramu.designsystem.theme.RamuTheme
import id.codemockup.ramu.feature.chat.components.ChatComposerBar
import id.codemockup.ramu.feature.chat.components.ChatMenuButton
import id.codemockup.ramu.feature.chat.components.ChatMessageList

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    onBack: (() -> Unit)? = null,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ChatContent(
        state,
        viewModel::editDraft,
        viewModel::send,
        viewModel::refresh,
        onBack = onBack,
    )
}

@Composable
fun ChatContent(
    state: ChatState,
    onDraftChange: (String) -> Unit,
    onSend: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    onNewSession: () -> Unit = {},
    onDeleteSession: () -> Unit = {},
) {
    val messages = state.conversation.data?.messages.orEmpty()
    val listState = rememberLazyListState()
    LaunchedEffect(messages.size, state.sending) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size + if (state.sending) 1 else 0)
    }
    AppPullToRefresh(
        isRefreshing = state.conversation.isLoading,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(AppColors.neutral.canvas)
                .appTopGlow()
                .safeDrawingPadding()
                .imePadding()
        ) {
            AppBar(
                title = "Hermes",
                subtitle = when {
                    state.sending -> "Thinking…"
                    state.conversation.isLoading -> "Connecting…"
                    state.needsRefresh -> "Connection needs attention"
                    else -> "Ready to chat"
                },
                leading = {
                    Surface(
                        modifier = Modifier.size(AppControlTokens.buttonSmall),
                        shape = RoundedCornerShape(AppRadius.small),
                        color = AppColors.neutral.surface,
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            AppMascot(
                                if (state.sending) AppMascotMood.Thinking else AppMascotMood.Happy,
                                size = AppControlTokens.buttonSmall - AppSpacing.sm.sm4
                            )
                        }
                    }
                },
                onBack = onBack,
                transparent = true,
                actions = { ChatMenuButton(onNewSession = onNewSession, onDeleteSession = onDeleteSession) },
            )

            ChatMessageList(
                messages = messages,
                sending = state.sending,
                modifier = Modifier.weight(1f),
                listState = listState,
                isLoading = state.conversation.isLoading,
            )
            if (state.conversation.errorMessage.isNotBlank()) {
                AppInlineAlert(
                    title = "Connection problem",
                    message = state.conversation.errorMessage,
                    variant = AppAlertVariant.Error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppSpacing.sm.sm12),
                    actionLabel = "Refresh history",
                    onAction = onRefresh.takeIf { !state.sending && !state.conversation.isLoading },
                )
            }
            ChatComposerBar(state.draft, state.canSend, onDraftChange, onSend)
        }

    }
}

@Preview(name = "Empty", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun EmptyChatPreview() {
    RamuTheme {
        ChatContent(
            state = ChatState(
                conversation = DataState(
                    data = ChatConversation(
                        "preview",
                        emptyList()
                    )
                )
            ),
            onDraftChange = {},
            onSend = {},
            onRefresh = {},
        )
    }
}

@Preview(name = "Conversation", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun ConversationPreview() {
    RamuTheme {
        ChatContent(
            state = ChatState(
                conversation = DataState(
                    data = ChatConversation(
                        sessionId = "preview",
                        messages = listOf(
                            ChatMessage(
                                "user-1",
                                "user",
                                "Can you help me plan dinners for the week?"
                            ),
                            ChatMessage(
                                "assistant-1",
                                "assistant",
                                "Of course. Which nights do you need quick meals?"
                            ),
                        ),
                    ),
                ),
                draft = "Monday and Wednesday",
            ),
            onDraftChange = {},
            onSend = {},
            onRefresh = {},
        )
    }
}

@Preview(name = "Sending", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun SendingPreview() {
    RamuTheme {
        ChatContent(
            state = ChatState(
                conversation = DataState(
                    data = ChatConversation(
                        sessionId = "preview",
                        messages = listOf(
                            ChatMessage(
                                "user-1",
                                "user",
                                "Can you help me plan dinners for the week?"
                            ),
                        ),
                    ),
                ),
                sending = true,
            ),
            onDraftChange = {},
            onSend = {},
            onRefresh = {},
        )
    }
}

@Preview(name = "Error (needs refresh)", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun ErrorPreview() {
    RamuTheme {
        ChatContent(
            state = ChatState(
                conversation = DataState(
                    data = ChatConversation("preview", emptyList()),
                    errorMessage = "Connection failed"
                ),
                needsRefresh = true,
            ),
            onDraftChange = {},
            onSend = {},
            onRefresh = {},
        )
    }
}

@Preview(name = "With back and more", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun WithNavAndMorePreview() {
    RamuTheme {
        ChatContent(
            state = ChatState(
                conversation = DataState(
                    data = ChatConversation(
                        sessionId = "preview",
                        messages = listOf(
                            ChatMessage("user-1", "user", "Hello Hermes"),
                            ChatMessage("assistant-1", "assistant", "Hi there!"),
                        ),
                    ),
                ),
            ),
            onDraftChange = {},
            onSend = {},
            onRefresh = {},
            onBack = {},
        )
    }
}
