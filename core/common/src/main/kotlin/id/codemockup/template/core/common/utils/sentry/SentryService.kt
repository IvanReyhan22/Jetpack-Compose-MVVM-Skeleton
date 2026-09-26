package id.codemockup.template.core.common.utils.sentry


import android.content.Context
import io.sentry.Sentry
import io.sentry.android.core.SentryAndroid
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.coroutines.CancellationException

object SentryService {
    @Volatile
    var isEnabled: Boolean = false
        private set

    fun init(context: Context, enabled: Boolean, dsn: String, environment: String, release: String) {
        if (!enabled || dsn.isBlank()) {
            if (isEnabled) Sentry.close()
            isEnabled = false
            return
        }
        SentryAndroid.init(context) { options ->
            options.dsn = dsn
            options.environment = environment
            options.release = release
            options.isSendDefaultPii = false
            options.isAttachScreenshot = false
            options.isEnableNdk = false
            options.tracesSampleRate = 0.0
            options.setBeforeSend { event, _ ->
                if (isExpectedFailure(event.throwable)) null else event
            }
        }
        isEnabled = Sentry.isEnabled()
    }

    internal fun isExpectedFailure(error: Throwable?): Boolean =
        generateSequence(error) { it.cause }.take(16).any {
            it is CancellationException || it is UnknownHostException ||
                it is ConnectException || it is SocketTimeoutException
        }
}
