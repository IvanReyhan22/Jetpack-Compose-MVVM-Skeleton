package id.codemockup.ramu.core.domain.repository.chat

import id.codemockup.ramu.core.data.remote.request.ChatRequest
import id.codemockup.ramu.core.data.remote.request.CreateChatSessionRequest
import id.codemockup.ramu.core.network.services.HermesServices
import javax.inject.Inject

class ChatDataSource @Inject constructor(private val services: HermesServices) : ChatRepository {
    override suspend fun createSession(request: CreateChatSessionRequest) =
        services.createSession(request)

    override suspend fun history(sessionId: String, inlineImages: Boolean) =
        services.history(sessionId, inlineImages)

    override suspend fun chat(sessionId: String, request: ChatRequest) =
        services.chat(sessionId, request)
}
