plugins {
    alias(libs.plugins.ramu.android.library)
}

android {
    namespace = "id.codemockup.ramu.core.extensions"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(libs.androidx.navigation.compose)
}
