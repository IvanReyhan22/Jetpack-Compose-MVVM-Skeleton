package id.codemockup.ramu.core.model.chat

data class ChatMessage(
    val id: String,
    val role: String,
    val text: String,
    val timestampMillis: Long? = null,
)
data class ChatConversation(val sessionId: String, val messages: List<ChatMessage>)
data class StoredChatSession(val baseUrl: String, val sessionId: String)
