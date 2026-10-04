package id.codemockup.ramu.core.network

import id.codemockup.ramu.core.data.remote.request.*
import id.codemockup.ramu.core.network.di.HermesClient
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*
import org.junit.Test
import retrofit2.HttpException

class HermesServicesTest {
    @Test fun sessionHistoryAndChatUseHermesContract() = runTest {
        MockWebServer().use { server ->
            server.start()
            val services = HermesClient.create(server.url("/").toString(), "test-key")
            server.enqueue(MockResponse().setBody("""{"session":{"id":"chat-1"}}"""))
            assertEquals("chat-1", services.createSession(CreateChatSessionRequest()).data.session.id)
            val create = server.takeRequest()
            assertEquals("/api/sessions", create.path)
            assertEquals("Bearer test-key", create.getHeader("Authorization"))
            assertEquals("POST", create.method)
            assertTrue(create.body.readUtf8().contains("Mobile chat"))
            server.enqueue(MockResponse().setBody("""{"session_id":"chat-2","data":[{"id":"m1","role":"user","content":"Hello","timestamp":1793284320.125}]}"""))
            val result = services.history("chat-1").data
            assertEquals("chat-2", result.session_id)
            assertEquals("Hello", result.data.single().content)
            assertEquals(1793284320.125, result.data.single().timestamp!!, 0.0)
            val history = server.takeRequest()
            assertEquals("/api/sessions/chat-1/messages?inline_images=false", history.path)
            assertEquals("GET", history.method)
            server.enqueue(MockResponse().setBody("""{"session_id":"chat-2","message":{"role":"assistant","content":"Hi"}}"""))
            assertEquals("Hi", services.chat("chat-1", ChatRequest("Hello")).data.message.content)
            val chat = server.takeRequest()
            assertEquals("/api/sessions/chat-1/chat", chat.path)
            assertEquals("{\"input\":\"Hello\"}", chat.body.readUtf8())
        }
    }
    @Test fun unauthorizedDoesNotRetryPost() = runTest {
        MockWebServer().use { server ->
            server.start()
            server.enqueue(MockResponse().setResponseCode(401).setBody("{}"))
            try { HermesClient.create(server.url("/").toString(), "wrong").chat("id", ChatRequest("Hello")); fail() }
            catch (error: HttpException) { assertEquals(401, error.code()) }
            assertEquals(1, server.requestCount)
        }
    }
    @Test fun missingKeyFailsBeforeNetworkCall() = runTest {
        MockWebServer().use { server ->
            server.start()
            try { HermesClient.create(server.url("/").toString(), "").createSession(CreateChatSessionRequest()); fail() }
            catch (error: java.io.IOException) { assertTrue(error.message!!.contains("app.properties")) }
            assertEquals(0, server.requestCount)
        }
    }
}
