package id.codemockup.ramu.core.network

import id.codemockup.ramu.core.common.ErrorMessageConstant
import id.codemockup.ramu.core.common.NetworkErrorManager
import id.codemockup.ramu.core.common.NetworkErrorType
import id.codemockup.ramu.core.common.SessionManager
import id.codemockup.ramu.core.datastore.BaseDataStore
import id.codemockup.ramu.core.model.session.User
import id.codemockup.ramu.core.model.session.UserSession
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.buffer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

class ResponseInterceptorTest {
    private val server = MockWebServer()
    private val store = MemoryStore()
    private val sessions = SessionManager()
    private val errors = NetworkErrorManager()
    private val client = OkHttpClient.Builder()
        .addInterceptor(ResponseInterceptor(store, sessions, errors)).build()

    @Before fun start() { server.start() }
    @After fun stop() { server.shutdown(); client.connectionPool.evictAll(); client.dispatcher.executorService.shutdown() }
    private fun request(auth: Boolean = false) = Request.Builder().url(server.url("/resource"))
        .apply { if (auth) header("@", "Auth") }.build()

    private fun failure(code: Int, body: String = "", auth: Boolean = false): ApiException {
        server.enqueue(MockResponse().setResponseCode(code).setBody(body))
        try { client.newCall(request(auth)).execute().close(); error("Expected failure") }
        catch (error: ApiException) { assertEquals(code, error.statusCode); return error }
    }

    @Test fun successPreservesBodyAndStripsAuthMarker() {
        server.enqueue(MockResponse().setBody("original response"))
        client.newCall(request(true)).execute().use { assertEquals("original response", it.body!!.string()) }
        val sent = server.takeRequest()
        assertNull(sent.getHeader("@"))
        assertEquals("Bearer old-token", sent.getHeader("Authorization"))
    }

    @Test fun mapsReferenceMessagesAndFallbacks() {
        assertEquals("Invalid field", failure(400, """{"data":"Invalid field"}""").message)
        assertEquals(ErrorMessageConstant.BAD_REQUEST, failure(400, "not json").message)
        assertEquals(ErrorMessageConstant.BAD_REQUEST, failure(400, """{"data":{"field":"bad"}}""").message)
        assertEquals("Unavailable", failure(503, """{"message":"Unavailable"}""").message)
        assertEquals(ErrorMessageConstant.INTERNAL_SERVER_ERROR, failure(500, """{"message":" "}""").message)
        assertEquals("Request failed (HTTP 403).", failure(403).message)
        assertEquals("Missing", failure(404, """{"message":"Missing"}""").message)
        assertEquals("Request failed (HTTP 429).", failure(429, "<html>too many</html>").message)
    }

    @Test fun publicUnauthorizedDoesNotInvalidateSession() {
        assertEquals(ErrorMessageConstant.ACCOUNT_NOT_FOUND, failure(401).message)
        assertEquals("old-token", store.session.value?.token)
        assertNull(sessions.events.value)
        assertNull(server.takeRequest().getHeader("Authorization"))
    }

    @Test fun protectedUnauthorizedClearsBeforePublishingAndRetainsPendingEvent() {
        assertEquals(ErrorMessageConstant.SESSION_EXPIRED, failure(401, auth = true).message)
        assertNull(store.session.value)
        assertFalse(sessions.events.value!!.needsClear)
        val id = sessions.events.value!!.id
        failure(401, auth = true)
        assertEquals(id, sessions.events.value!!.id)
    }

    @Test fun staleUnauthorizedCannotDeleteNewSession() {
        server.enqueue(MockResponse().setResponseCode(401))
        val delayed = client.newBuilder().addInterceptor { chain ->
            store.session.value = UserSession("new-token", User("new", "new@example.com"))
            chain.proceed(chain.request())
        }.build()
        try { delayed.newCall(request(true)).execute().close(); fail() } catch (_: ApiException) { }
        assertEquals("new-token", store.session.value?.token)
        assertNull(sessions.events.value)
    }

    @Test fun storageFailureKeepsSessionAndPublishesRetryableEvent() {
        store.failWrite = true
        server.enqueue(MockResponse().setResponseCode(401))
        try { client.newCall(request(true)).execute().close(); fail() }
        catch (error: IOException) { assertEquals(ErrorMessageConstant.SESSION_STORAGE_ERROR, error.message) }
        assertNotNull(store.session.value)
        assertTrue(sessions.events.value!!.needsClear)
    }

    @Test fun transportErrorsPreserveCauseAndPublishGlobalCategories() {
        listOf(
            UnknownHostException() to NetworkErrorType.SERVER_UNREACHABLE,
            ConnectException() to NetworkErrorType.NO_CONNECTION,
            SocketTimeoutException() to NetworkErrorType.TIMEOUT,
        ).forEach { (cause, category) ->
            val failing = client.newBuilder().addInterceptor { throw cause }.build()
            try { failing.newCall(request()).execute().close(); fail() }
            catch (error: IOException) { assertSame(cause, error.cause); assertEquals(category.message, error.message) }
            assertEquals(category, errors.events.value)
            errors.acknowledge(category)
        }
        val tls = SSLHandshakeException("certificate rejected")
        try { client.newBuilder().addInterceptor { throw tls }.build().newCall(request()).execute().close(); fail() }
        catch (error: IOException) { assertSame(tls, error.cause); assertEquals(ErrorMessageConstant.UNTRUSTED_CONNECTION, error.message) }
        assertNull(errors.events.value)
    }

    @Test fun cancellationDoesNotShowNetworkDialog() {
        val cancellation = CancellationException("cancelled")
        try { client.newBuilder().addInterceptor { throw cancellation }.build().newCall(request()).execute().close(); fail() }
        catch (error: CancellationException) { assertSame(cancellation, error) }
        assertNull(errors.events.value)
    }

    @Test fun failedResponseIsClosedAndBodyParsingIsBounded() {
        var closed = false
        var read = 0L
        val source = object : ForwardingSource(Buffer().writeUtf8("x".repeat(200_000))) {
            override fun read(sink: Buffer, byteCount: Long): Long = super.read(sink, byteCount).also { if (it > 0) read += it }
            override fun close() { closed = true; super.close() }
        }.buffer()
        val body = object : ResponseBody() {
            override fun contentType() = "text/plain".toMediaType()
            override fun contentLength() = 200_000L
            override fun source(): BufferedSource = source
        }
        server.enqueue(MockResponse().setResponseCode(500))
        val custom = client.newBuilder().addInterceptor { chain ->
            chain.proceed(chain.request()).let { response ->
                response.body?.close()
                response.newBuilder().body(body).build()
            }
        }.build()
        try { custom.newCall(request()).execute().close(); fail() } catch (_: ApiException) { }
        assertTrue(closed)
        assertTrue("read=$read", read <= 64 * 1024L)
    }

    @Test fun sensitiveCallsBypassInspectionWithoutLeakingMarker() {
        var captures = 0
        val inspector = Interceptor { chain -> captures++; chain.proceed(chain.request()) }
        val inspected = client.newBuilder().addInterceptor(DiagnosticsInterceptor(inspector)).build()
        server.enqueue(MockResponse().setBody("ok"))
        inspected.newCall(request().newBuilder().header("X-Sensitive-Body", "true").build()).execute().close()
        assertEquals(0, captures)
        assertNull(server.takeRequest().getHeader("X-Sensitive-Body"))
        server.enqueue(MockResponse().setResponseCode(400))
        try { inspected.newCall(request()).execute().close(); fail() } catch (_: ApiException) { }
        assertEquals(1, captures)
    }

    private class MemoryStore : BaseDataStore {
        override val session = MutableStateFlow<UserSession?>(UserSession("old-token", User("id", "demo@example.com")))
        var failWrite = false
        override suspend fun saveSession(session: UserSession) { this.session.value = session }
        override suspend fun clearSession() { session.value = null }
        override suspend fun clearSessionIfTokenMatches(expectedToken: String): Boolean {
            if (failWrite) throw IOException("Storage unavailable")
            if (session.value?.token != expectedToken) return false
            session.value = null
            return true
        }
    }
}
