package id.codemockup.ramu.core.domain.usecase.chat

import id.codemockup.ramu.core.common.UiState
import id.codemockup.ramu.core.data.remote.request.CreateChatSessionRequest
import id.codemockup.ramu.core.datastore.ChatSessionStore
import id.codemockup.ramu.core.domain.mapper.toChatMessage
import id.codemockup.ramu.core.domain.repository.chat.ChatRepository
import id.codemockup.ramu.core.model.chat.ChatConversation
import id.codemockup.ramu.core.model.chat.StoredChatSession
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named

class OpenChatUseCase @Inject constructor(
    private val repository: ChatRepository,
    private val store: ChatSessionStore,
    @param:Named("HermesBaseUrl") private val baseUrl: String,
) {
    operator fun invoke(): Flow<UiState<ChatConversation>> = flow {
        emit(UiState.Loading)
        val result = try {
            UiState.Success(openChat())
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (error: Exception) {
            UiState.Error(chatErrorMessage(error))
        }
        emit(result)
    }

    private suspend fun openChat(): ChatConversation {
        val saved = store.read()?.takeIf { it.baseUrl == baseUrl }
        if (saved != null) {
            try {
                val response = repository.history(saved.sessionId).data
                saveSession(response.session_id)
                return ChatConversation(
                    response.session_id,
                    response.data.mapIndexedNotNull { index, message -> message.toChatMessage(index) },
                )
            } catch (error: HttpException) {
                if (error.code() != 404) throw error
            }
        }
        val sessionId = repository.createSession(CreateChatSessionRequest()).data.session.id
        saveSession(sessionId)
        return ChatConversation(sessionId, emptyList())
    }

    private suspend fun saveSession(sessionId: String) {
        require(sessionId.isNotBlank()) { "Hermes returned an empty session ID." }
        store.save(StoredChatSession(baseUrl, sessionId))
    }
}
