plugins {
    alias(libs.plugins.ramu.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "id.codemockup.ramu.core.data"
}

dependencies {
    implementation(projects.core.model)
    api(libs.kotlinx.serialization.json)
}
