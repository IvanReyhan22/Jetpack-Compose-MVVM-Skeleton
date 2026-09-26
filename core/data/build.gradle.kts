plugins {
    alias(libs.plugins.template.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "id.codemockup.template.core.data"
}

dependencies {
    implementation(projects.core.model)
    api(libs.kotlinx.serialization.json)
}
