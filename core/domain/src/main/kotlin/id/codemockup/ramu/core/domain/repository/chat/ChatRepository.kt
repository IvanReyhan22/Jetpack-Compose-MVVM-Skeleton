package id.codemockup.ramu.core.domain.repository.chat

import id.codemockup.ramu.core.data.remote.request.ChatRequest
import id.codemockup.ramu.core.data.remote.request.CreateChatSessionRequest
import id.codemockup.ramu.core.data.remote.response.Response
import id.codemockup.ramu.core.data.remote.response.chat.ChatHistoryResponse
import id.codemockup.ramu.core.data.remote.response.chat.ChatResponse
import id.codemockup.ramu.core.data.remote.response.chat.ChatSessionResponse

interface ChatRepository {
    suspend fun createSession(request: CreateChatSessionRequest): Response<ChatSessionResponse>
    suspend fun history(sessionId: String, inlineImages: Boolean = false): Response<ChatHistoryResponse>
    suspend fun chat(sessionId: String, request: ChatRequest): Response<ChatResponse>
}
