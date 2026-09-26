package id.codemockup.template

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import id.codemockup.template.core.common.utils.sentry.SentryService

@HiltAndroidApp
class TemplateApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SentryService.init(
            context = this,
            enabled = BuildConfig.SENTRY_ENABLED,
            dsn = BuildConfig.SENTRY_DSN,
            environment = BuildConfig.FLAVOR,
            release = "${BuildConfig.APPLICATION_ID}@${BuildConfig.VERSION_NAME}+${BuildConfig.VERSION_CODE}",
        )
    }
}
