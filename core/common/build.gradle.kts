plugins {
    alias(libs.plugins.template.android.library)
    alias(libs.plugins.template.android.hilt)
}

android {
    namespace = "id.codemockup.template.core.common"
}

dependencies {
    api(libs.coroutines.android)
    implementation(libs.sentry.android)
}
