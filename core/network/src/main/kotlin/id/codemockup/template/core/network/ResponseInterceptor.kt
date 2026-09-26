package id.codemockup.template.core.network


import id.codemockup.template.core.common.ErrorMessageConstant
import id.codemockup.template.core.common.NetworkErrorManager
import id.codemockup.template.core.common.NetworkErrorType
import id.codemockup.template.core.common.SessionManager
import id.codemockup.template.core.datastore.BaseDataStore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLException

class ResponseInterceptor @Inject constructor(
    private val dataStore: BaseDataStore,
    private val sessionManager: SessionManager,
    private val networkErrorManager: NetworkErrorManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val authenticated = original.headers.values("@").contains("Auth")
        val token = if (authenticated) runBlocking { dataStore.session.first()?.token } else null
        val request = original.newBuilder().removeHeader("@").apply {
            if (authenticated) {
                removeHeader("Authorization")
                if (!token.isNullOrBlank()) header("Authorization", "Bearer $token")
            }
        }.build()
        val response = try {
            chain.proceed(request)
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (error: IOException) {
            if (chain.call().isCanceled()) throw error
            val message = when (error) {
                is UnknownHostException -> {
                    networkErrorManager.onNetworkError(NetworkErrorType.SERVER_UNREACHABLE)
                    ErrorMessageConstant.SERVER_UNREACHABLE
                }

                is SocketTimeoutException -> {
                    networkErrorManager.onNetworkError(NetworkErrorType.TIMEOUT)
                    ErrorMessageConstant.REQUEST_TIMEOUT
                }

                is ConnectException -> {
                    networkErrorManager.onNetworkError(NetworkErrorType.NO_CONNECTION)
                    ErrorMessageConstant.NO_CONNECTION
                }

                is SSLException -> ErrorMessageConstant.UNTRUSTED_CONNECTION
                else -> error.message ?: "Request failed. Please try again."
            }
            throw IOException(message, error)
        }
        if (response.isSuccessful) return response
        val code = response.code
        val message = response.use {
            val body = try {
                it.peekBody(64 * 1024L).string()
            } catch (_: IOException) {
                ""
            }
            HttpErrorMapper.message(code, body, authenticated)
        }
        if (code == 401 && !token.isNullOrBlank()) {
            try {
                if (runBlocking { dataStore.clearSessionIfTokenMatches(token) }) {
                    sessionManager.onSessionExpired(token)
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                sessionManager.onSessionExpired(token, needsClear = true)
                throw IOException(ErrorMessageConstant.SESSION_STORAGE_ERROR, error)
            }
        }
        throw ApiException(code, message)
    }
}
