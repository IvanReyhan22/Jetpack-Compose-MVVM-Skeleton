plugins {
    alias(libs.plugins.template.android.library)
}

android {
    namespace = "id.codemockup.template.core.extensions"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(libs.androidx.navigation.compose)
}
