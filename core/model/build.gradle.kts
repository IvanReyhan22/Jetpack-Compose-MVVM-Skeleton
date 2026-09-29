plugins {
    alias(libs.plugins.ramu.android.library)
}

android {
    namespace = "id.codemockup.ramu.core.model"
}

dependencies {
    implementation(projects.core.common)
}
