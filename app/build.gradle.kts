plugins {
    alias(libs.plugins.template.android.application)
    alias(libs.plugins.template.android.application.compose)
    alias(libs.plugins.template.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "id.codemockup.template"
    buildFeatures { buildConfig = true }
    defaultConfig {
        buildConfigField("boolean", "SENTRY_ENABLED", "false")
        buildConfigField("String", "SENTRY_DSN", "\"\"")
        applicationId = "id.codemockup.template"
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }
    productFlavors {
        named("staging") { applicationIdSuffix = ".staging"; versionNameSuffix = "-staging" }
    }
    buildTypes { release { isMinifyEnabled = false } }
}
dependencies {
    implementation(projects.feature.login)
    implementation(projects.feature.main)
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.datastore)
    implementation(projects.core.designsystem)
    implementation(projects.core.extensions)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coroutines.android)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.test.junit4)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.compose.test.manifest)
    debugImplementation(projects.core.network)
    debugImplementation(libs.okhttp)
    androidTestImplementation(libs.sentry.android)
}
