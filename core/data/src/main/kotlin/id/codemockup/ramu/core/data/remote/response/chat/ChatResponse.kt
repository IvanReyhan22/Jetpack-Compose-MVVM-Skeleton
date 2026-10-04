package id.codemockup.ramu.core.data.remote.response.chat

data class ChatSessionDto(val id: String)
data class ChatSessionResponse(val session: ChatSessionDto)
data class ChatMessageDto(
    val id: String? = null,
    val role: String,
    val content: Any? = null,
    val display_kind: String? = null,
    val timestamp: Double? = null,
)

data class ChatHistoryResponse(val session_id: String, val data: List<ChatMessageDto>)
data class ChatResponse(val session_id: String, val message: ChatMessageDto)
