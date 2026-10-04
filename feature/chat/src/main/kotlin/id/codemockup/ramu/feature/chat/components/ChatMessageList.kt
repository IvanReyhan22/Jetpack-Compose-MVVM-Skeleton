package id.codemockup.ramu.feature.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import id.codemockup.ramu.core.model.chat.ChatMessage
import id.codemockup.ramu.core.extensions.toRelativeDateTimeLabel
import id.codemockup.ramu.designsystem.common.enums.AppMessageAuthor
import id.codemockup.ramu.designsystem.components.feedback.AppTypingBubble
import id.codemockup.ramu.designsystem.components.feedback.AppMessageBubble
import id.codemockup.ramu.designsystem.components.icons.AppMascotTile
import id.codemockup.ramu.designsystem.theme.AppColors
import id.codemockup.ramu.designsystem.theme.AppRadius
import id.codemockup.ramu.designsystem.theme.AppSpacing
import id.codemockup.ramu.designsystem.theme.RamuTheme

private const val BUBBLE_MAX_WIDTH_FRACTION = 0.84f

@Composable
fun ChatMessageList(
    messages: List<ChatMessage>,
    sending: Boolean,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    isLoading: Boolean = false,
) {
    BoxWithConstraints(modifier.fillMaxWidth()) {
        val maxBubbleWidth = maxWidth * BUBBLE_MAX_WIDTH_FRACTION
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .testTag("chat_messages"),
            state = listState,
            contentPadding = PaddingValues(AppSpacing.md.md16),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm.sm12),
        ) {
            item(key = "intro", contentType = "intro") {
                ChatEmptyState(
                    isLoading = isLoading,
                    date = messages.firstOrNull()?.timestampMillis?.toRelativeDateTimeLabel(),
                )
            }
            items(messages, key = { it.id }, contentType = { it.role }) { message ->
                val user = message.role == "user"
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = if (user) Arrangement.End else Arrangement.Start,
                ) {
                    AppMessageBubble(
                        text = message.text,
                        author = if (user) AppMessageAuthor.User else AppMessageAuthor.Hermes,
                        modifier = Modifier.widthIn(max = maxBubbleWidth),
                        avatar = if (user) null else {
                            { AppMascotTile(size = AppSpacing.lg.lg32, cornerRadius = AppRadius.small) }
                        },
                    )
                }
            }
            if (sending) {
                item(key = "typing", contentType = "typing") {
                    AppTypingBubble(
                        avatar = { AppMascotTile(size = AppSpacing.lg.lg32, cornerRadius = AppRadius.small) },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 500)
@Composable
private fun ChatMessageListPreview() {
    RamuTheme {
        ChatMessageList(
            messages = listOf(
                ChatMessage("user-1", "user", "Can you help me plan dinners for the week?"),
                ChatMessage("assistant-1", "assistant", "Of course. Which nights do you need quick meals?"),
            ),
            sending = true,
        )
    }
}
