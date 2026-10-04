package id.codemockup.ramu.core.domain

import id.codemockup.ramu.core.data.remote.request.ChatRequest
import id.codemockup.ramu.core.data.remote.request.CreateChatSessionRequest
import id.codemockup.ramu.core.data.remote.response.Response
import id.codemockup.ramu.core.data.remote.response.chat.*
import id.codemockup.ramu.core.domain.repository.chat.ChatDataSource
import id.codemockup.ramu.core.network.services.HermesServices
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class ChatRepositoryTest {
    private class Services : HermesServices {
        val session = Response(ChatSessionResponse(ChatSessionDto("created")))
        val history = Response(ChatHistoryResponse("saved", emptyList()))
        val reply = Response(ChatResponse("rotated", ChatMessageDto(role = "assistant", content = "Reply")))
        var createRequest: CreateChatSessionRequest? = null
        var historyArguments: Pair<String, Boolean>? = null
        var chatArguments: Pair<String, ChatRequest>? = null
        var failure: Exception? = null

        override suspend fun createSession(request: CreateChatSessionRequest): Response<ChatSessionResponse> {
            failure?.let { throw it }
            createRequest = request
            return session
        }
        override suspend fun history(sessionId: String, inlineImages: Boolean): Response<ChatHistoryResponse> {
            historyArguments = sessionId to inlineImages
            return history
        }
        override suspend fun chat(sessionId: String, request: ChatRequest): Response<ChatResponse> {
            chatArguments = sessionId to request
            return reply
        }
    }

    @Test fun delegatesArgumentsAndReturnsUnmodifiedServiceResponses() = runTest {
        val services = Services()
        val repository = ChatDataSource(services)
        val createRequest = CreateChatSessionRequest("Custom title")
        val chatRequest = ChatRequest("Next question")
        assertSame(services.session, repository.createSession(createRequest))
        assertSame(createRequest, services.createRequest)
        assertSame(services.history, repository.history("saved"))
        assertEquals("saved" to false, services.historyArguments)
        repository.history("other", true)
        assertEquals("other" to true, services.historyArguments)
        assertSame(services.reply, repository.chat("saved", chatRequest))
        assertEquals("saved" to chatRequest, services.chatArguments)
    }

    @Test fun propagatesServiceFailureAndCancellation() = runTest {
        val services = Services()
        val repository = ChatDataSource(services)
        for (error in listOf(java.io.IOException("Disconnected"), CancellationException("cancelled"))) {
            services.failure = error
            try {
                repository.createSession(CreateChatSessionRequest())
                fail("Expected service failure")
            } catch (actual: Exception) { assertSame(error, actual) }
        }
    }
}
