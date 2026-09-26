package id.codemockup.template.core.network


import id.codemockup.template.core.common.utils.sentry.SentryLogger
import id.codemockup.template.core.common.utils.sentry.SentryService
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class SentryBreadcrumbInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!SentryService.isEnabled) return chain.proceed(chain.request())
        val request = chain.request()
        val url = request.url.newBuilder().query(null).fragment(null).username("").password("").build().toString()
        return try {
            chain.proceed(request).also { SentryLogger.apiBreadcrumb(url, request.method, it.code) }
        } catch (error: IOException) {
            SentryLogger.apiBreadcrumb(url, request.method, null)
            throw error
        }
    }
}
