package id.codemockup.ramu.feature.chat

import id.codemockup.ramu.core.domain.repository.chat.ChatRepository
import id.codemockup.ramu.core.datastore.ChatSessionStore
import id.codemockup.ramu.core.data.remote.request.ChatRequest
import id.codemockup.ramu.core.data.remote.request.CreateChatSessionRequest
import id.codemockup.ramu.core.data.remote.response.Response
import id.codemockup.ramu.core.data.remote.response.chat.*
import id.codemockup.ramu.core.domain.usecase.chat.*
import id.codemockup.ramu.core.model.chat.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {
    @get:Rule
    internal val main = MainDispatcherRule()

    private class Repository : ChatRepository {
        var sends = 0
        var failOpen = false
        var sendResult = CompletableDeferred<ChatResponse>()
        override suspend fun createSession(request: CreateChatSessionRequest) =
            Response(ChatSessionResponse(ChatSessionDto("saved")))

        override suspend fun history(sessionId: String, inlineImages: Boolean): Response<ChatHistoryResponse> {
            if (failOpen) error("Unavailable")
            return Response(ChatHistoryResponse("saved", listOf(ChatMessageDto("1", "assistant", "History"))))
        }

        override suspend fun chat(sessionId: String, request: ChatRequest): Response<ChatResponse> {
            sends++
            return Response(sendResult.await())
        }
    }

    private class Store : ChatSessionStore {
        var session = StoredChatSession("host", "saved")
        override suspend fun read() = session
        override suspend fun save(session: StoredChatSession) { this.session = session }
    }

    private fun model(repository: Repository): ChatViewModel {
        val store = Store()
        return ChatViewModel(ChatUseCase(
            OpenChatUseCase(repository, store, "host"),
            SendChatUseCase(repository, store, "host"),
        ))
    }

    @Test
    fun blocksBlankAndDuplicateSendsAndPreservesNextDraft() = runTest {
        val repository = Repository()
        val vm = model(repository)
        advanceUntilIdle()
        vm.editDraft("  "); vm.send(); assertEquals(0, repository.sends)
        vm.editDraft("Hello"); vm.send(); vm.send(); runCurrent()
        assertEquals(1, repository.sends)
        assertTrue(vm.state.value.sending)
        vm.editDraft("Next draft")
        repository.sendResult.complete(
            ChatResponse("rotated", ChatMessageDto("2", "assistant", "Reply"))
        )
        advanceUntilIdle()
        assertFalse(vm.state.value.sending)
        assertEquals("Next draft", vm.state.value.draft)
        assertEquals(
            listOf("History", "Hello", "Reply"),
            vm.state.value.conversation.data!!.messages.map { it.text })
        assertEquals("rotated", vm.state.value.conversation.data!!.sessionId)
    }

    @Test
    fun failedSendRequiresHistoryRefreshAndNeverResends() = runTest {
        val repository = Repository()
        val vm = model(repository)
        advanceUntilIdle()
        vm.editDraft("Hello"); vm.send(); runCurrent()
        repository.sendResult.completeExceptionally(java.io.IOException("Disconnected"))
        advanceUntilIdle()
        assertTrue(vm.state.value.needsRefresh)
        vm.editDraft("Next"); vm.send(); runCurrent()
        assertEquals(1, repository.sends)
        repository.failOpen = true; vm.refresh(); advanceUntilIdle()
        assertFalse(vm.state.value.canSend)
        repository.failOpen = false; vm.refresh(); advanceUntilIdle()
        assertTrue(vm.state.value.canSend)
        assertEquals(listOf("History"), vm.state.value.conversation.data!!.messages.map { it.text })
    }

    @Test
    fun startupFailureIsRetryableAndRestoresHistory() = runTest {
        val repository = Repository().apply { failOpen = true }
        val vm = model(repository)
        advanceUntilIdle()
        assertTrue(vm.state.value.conversation.errorMessage.isNotBlank())
        repository.failOpen = false; vm.refresh(); advanceUntilIdle()
        assertEquals("saved", vm.state.value.conversation.data!!.sessionId)
    }
}
