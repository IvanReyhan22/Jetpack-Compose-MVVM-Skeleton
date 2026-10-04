package id.codemockup.ramu.feature.chat

import id.codemockup.ramu.core.common.DataState
import id.codemockup.ramu.core.model.chat.ChatConversation

data class ChatState(
    val conversation: DataState<ChatConversation> = DataState(isLoading = true),
    val draft: String = "",
    val sending: Boolean = false,
    val needsRefresh: Boolean = false,
) {
    val canSend: Boolean get() = !conversation.isLoading && !sending && !needsRefresh && conversation.data != null && draft.isNotBlank()
}
