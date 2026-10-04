package id.codemockup.ramu.core.domain

import id.codemockup.ramu.core.common.UiState
import id.codemockup.ramu.core.data.remote.request.ChatRequest
import id.codemockup.ramu.core.data.remote.request.CreateChatSessionRequest
import id.codemockup.ramu.core.data.remote.response.Response
import id.codemockup.ramu.core.data.remote.response.chat.*
import id.codemockup.ramu.core.datastore.ChatSessionStore
import id.codemockup.ramu.core.domain.mapper.toChatMessage
import id.codemockup.ramu.core.domain.repository.chat.ChatRepository
import id.codemockup.ramu.core.domain.usecase.chat.OpenChatUseCase
import id.codemockup.ramu.core.domain.usecase.chat.SendChatUseCase
import id.codemockup.ramu.core.model.chat.ChatConversation
import id.codemockup.ramu.core.model.chat.StoredChatSession
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response as HttpResponse

class ChatUseCaseTest {
    private class Store : ChatSessionStore {
        var value: StoredChatSession? = null
        var failWrite = false
        var failRead = false
        override suspend fun read(): StoredChatSession? {
            if (failRead) error("Read failed")
            return value
        }
        override suspend fun save(session: StoredChatSession) {
            if (failWrite) error("Disk full")
            value = session
        }
    }

    private class Repository : ChatRepository {
        var creations = 0
        var historyFailure: Exception? = null
        var sendFailure: Exception? = null
        var historySessionId: String? = null
        var sent: Pair<String, ChatRequest>? = null
        var reply = ChatResponse("rotated", ChatMessageDto(role = "assistant", content = "Reply"))
        override suspend fun createSession(request: CreateChatSessionRequest): Response<ChatSessionResponse> {
            creations++
            return Response(ChatSessionResponse(ChatSessionDto("created")))
        }
        override suspend fun history(sessionId: String, inlineImages: Boolean): Response<ChatHistoryResponse> {
            historySessionId = sessionId
            historyFailure?.let { throw it }
            return Response(ChatHistoryResponse("resolved", listOf(
                ChatMessageDto("1", "user", "Hello"),
                ChatMessageDto("2", "tool", "hidden"),
                ChatMessageDto("3", "assistant", "Thinking", "commentary"),
                ChatMessageDto("4", "assistant", "Hi"),
            )))
        }
        override suspend fun chat(sessionId: String, request: ChatRequest): Response<ChatResponse> {
            sendFailure?.let { throw it }
            sent = sessionId to request
            return Response(reply)
        }
    }

    private suspend fun Flow<UiState<ChatConversation>>.successfulConversation(): ChatConversation {
        val results = toList()
        assertEquals(2, results.size)
        assertEquals(UiState.Loading, results.first())
        return (results.last() as UiState.Success).data
    }

    @Test fun restoresServerHistoryAndPersistsResolvedSessionBeforeSuccess() = runTest {
        val store = Store().apply { value = StoredChatSession("host", "saved") }
        val repository = Repository()
        val restored = OpenChatUseCase(repository, store, "host")().successfulConversation()
        assertEquals(listOf("Hello", "Hi"), restored.messages.map { it.text })
        assertEquals("saved", repository.historySessionId)
        assertEquals("resolved", store.value!!.sessionId)
        assertEquals(0, repository.creations)
    }

    @Test fun createsMissingSessionAndStartsFreshAfterHostChange() = runTest {
        val store = Store().apply { value = StoredChatSession("host", "missing") }
        val repository = Repository().apply { historyFailure = httpFailure(404) }
        assertEquals("created", OpenChatUseCase(repository, store, "host")().successfulConversation().sessionId)
        assertEquals(StoredChatSession("host", "created"), store.value)
        OpenChatUseCase(repository, store, "other-host")().successfulConversation()
        assertEquals(2, repository.creations)
        assertEquals(StoredChatSession("other-host", "created"), store.value)
    }

    @Test fun createsAndPersistsFirstSession() = runTest {
        val store = Store()
        val repository = Repository()
        val conversation = OpenChatUseCase(repository, store, "host")().successfulConversation()
        assertTrue(conversation.messages.isEmpty())
        assertEquals(StoredChatSession("host", conversation.sessionId), store.value)
        assertNull(repository.historySessionId)
    }

    @Test fun sendsInputAndPersistsRotatedSessionBeforeSuccess() = runTest {
        val store = Store()
        val repository = Repository()
        val sent = SendChatUseCase(repository, store, "host")("saved", "Next").successfulConversation()
        assertEquals("saved" to ChatRequest("Next"), repository.sent)
        assertEquals(StoredChatSession("host", "rotated"), store.value)
        assertEquals("Reply", sent.messages.single().text)
        assertEquals("assistant", sent.messages.single().role)
    }

    @Test fun storageFailuresProduceErrorAndRemainRetryable() = runTest {
        val store = Store().apply { failWrite = true }
        val repository = Repository()
        val open = OpenChatUseCase(repository, store, "host")
        val openResults = open().toList()
        assertEquals(UiState.Loading, openResults.first())
        assertEquals(UiState.Error("Disk full"), openResults.last())
        assertEquals(UiState.Error("Disk full"), SendChatUseCase(repository, store, "host")("saved", "Next").toList().last())
        store.failWrite = false
        open().successfulConversation()
        store.failRead = true
        assertEquals(UiState.Error("Read failed"), open().toList().last())
    }

    @Test fun authenticationAndRateLimitFailuresDoNotCreateReplacementSession() = runTest {
        val store = Store().apply { value = StoredChatSession("host", "saved") }
        val repository = Repository()
        for (code in listOf(401, 403, 429)) {
            repository.historyFailure = httpFailure(code)
            repository.sendFailure = httpFailure(code)
            val expected = if (code == 429) "Hermes is busy. Wait before sending another message."
                else "Hermes rejected the API key. Check app.properties and rebuild."
            assertEquals(UiState.Error(expected), OpenChatUseCase(repository, store, "host")().toList().last())
            assertEquals(UiState.Error(expected), SendChatUseCase(repository, store, "host")("saved", "Next").toList().last())
        }
        assertEquals(0, repository.creations)
        assertEquals("saved", store.value!!.sessionId)
    }

    @Test fun cancellationPropagatesWithoutErrorEmission() = runTest {
        val store = Store().apply { value = StoredChatSession("host", "saved") }
        val cancellation = CancellationException("cancelled")
        val repository = Repository().apply { historyFailure = cancellation; sendFailure = cancellation }
        val flows = listOf(OpenChatUseCase(repository, store, "host")(), SendChatUseCase(repository, store, "host")("saved", "Next"))
        for (flow in flows) {
            val results = mutableListOf<UiState<ChatConversation>>()
            try { flow.toList(results); fail("Expected cancellation") }
            catch (actual: CancellationException) { assertSame(cancellation, actual) }
            assertEquals(listOf(UiState.Loading), results)
        }
    }

    @Test fun collectorFailureIsNotConvertedToServiceError() = runTest {
        val repository = Repository()
        val collectorFailure = IllegalStateException("Collector failed")
        try {
            OpenChatUseCase(repository, Store(), "host")().collect {
                if (it is UiState.Success) throw collectorFailure
            }
            fail("Expected collector failure")
        } catch (actual: IllegalStateException) { assertSame(collectorFailure, actual) }
    }

    @Test fun missingFinalTextKeepsReturnedSessionAndProducesVisibleError() = runTest {
        val store = Store()
        val repository = Repository().apply { reply = ChatResponse("rotated", ChatMessageDto(role = "assistant", content = "")) }
        val results = SendChatUseCase(repository, store, "host")("saved", "Next").toList()
        assertEquals(UiState.Error("Hermes returned no final text. Refresh history before sending again."), results.last())
        assertEquals("rotated", store.value!!.sessionId)
    }

    @Test fun structuredContentRetainsOnlyText() {
        val dto = ChatMessageDto(role = "assistant", content = listOf(
            mapOf("type" to "text", "text" to "Answer"),
            mapOf("type" to "image_url", "image_url" to "private"),
        ))
        assertEquals("Answer", dto.toChatMessage(0)!!.text)
    }

    @Test fun messageTimestampConvertsEpochSecondsToMilliseconds() {
        val dto = ChatMessageDto(role = "user", content = "Hello", timestamp = 1793284320.125)
        assertEquals(1793284320125L, dto.toChatMessage(0)!!.timestampMillis)
        assertNull(dto.copy(timestamp = null).toChatMessage(0)!!.timestampMillis)
        assertNull(dto.copy(timestamp = Double.NaN).toChatMessage(0)!!.timestampMillis)
    }

    private fun httpFailure(code: Int) = HttpException(HttpResponse.error<Any>(code, "{}".toResponseBody()))
}
