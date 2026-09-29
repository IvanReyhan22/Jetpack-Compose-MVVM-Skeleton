plugins {
    alias(libs.plugins.ramu.android.library)
    alias(libs.plugins.ramu.android.hilt)
}

android {
    namespace = "id.codemockup.ramu.core.common"
}

dependencies {
    api(libs.coroutines.android)
    implementation(libs.sentry.android)
}
