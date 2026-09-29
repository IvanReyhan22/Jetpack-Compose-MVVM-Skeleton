plugins {
    alias(libs.plugins.ramu.android.library)
    alias(libs.plugins.ramu.android.library.compose)
}

android {
    namespace = "id.codemockup.ramu.designsystem"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.extensions)
}
