package id.codemockup.ramu.core.network


import okhttp3.Interceptor
import okhttp3.Response

class DiagnosticsInterceptor(private val inspector: Interceptor) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (chain.request().header(SENSITIVE_BODY_HEADER) != null) {
            return chain.proceed(chain.request().newBuilder().removeHeader(SENSITIVE_BODY_HEADER).build())
        }
        return inspector.intercept(chain)
    }

    companion object { const val SENSITIVE_BODY_HEADER = "X-Sensitive-Body" }
}
