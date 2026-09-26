package id.codemockup.template.core.common.utils.sentry


import io.sentry.Breadcrumb
import io.sentry.Sentry

object SentryLogger {
    fun captureException(error: Throwable) {
        if (SentryService.isEnabled && !SentryService.isExpectedFailure(error)) Sentry.captureException(error)
    }

    fun apiBreadcrumb(url: String, method: String, statusCode: Int?) {
        if (!SentryService.isEnabled) return
        Sentry.addBreadcrumb(Breadcrumb().apply {
            category = "http"
            type = "http"
            setData("url", url)
            setData("method", method)
            statusCode?.let { setData("status_code", it) }
        })
    }
}
