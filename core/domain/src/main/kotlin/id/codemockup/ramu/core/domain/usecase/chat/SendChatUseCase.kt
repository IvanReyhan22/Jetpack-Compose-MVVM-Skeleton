package id.codemockup.ramu.core.domain.usecase.chat

import id.codemockup.ramu.core.common.UiState
import id.codemockup.ramu.core.data.remote.request.ChatRequest
import id.codemockup.ramu.core.datastore.ChatSessionStore
import id.codemockup.ramu.core.domain.mapper.toChatMessage
import id.codemockup.ramu.core.domain.repository.chat.ChatRepository
import id.codemockup.ramu.core.model.chat.ChatConversation
import id.codemockup.ramu.core.model.chat.StoredChatSession
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named

class SendChatUseCase @Inject constructor(
    private val repository: ChatRepository,
    private val store: ChatSessionStore,
    @Named("HermesBaseUrl") private val baseUrl: String,
) {
    operator fun invoke(sessionId: String, input: String): Flow<UiState<ChatConversation>> = flow {
        emit(UiState.Loading)
        val result = try {
            UiState.Success(sendChat(sessionId, input))
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (error: Exception) {
            UiState.Error(chatErrorMessage(error))
        }
        emit(result)
    }

    private suspend fun sendChat(sessionId: String, input: String): ChatConversation {
        val response = repository.chat(sessionId, ChatRequest(input)).data
        require(response.session_id.isNotBlank()) { "Hermes returned an empty session ID." }
        store.save(StoredChatSession(baseUrl, response.session_id))
        val message = response.message.toChatMessage(0)
            ?: error("Hermes returned no final text. Refresh history before sending again.")
        return ChatConversation(
            response.session_id,
            listOf(message.copy(id = UUID.randomUUID().toString())),
        )
    }
}
