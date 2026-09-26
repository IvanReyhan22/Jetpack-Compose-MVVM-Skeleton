plugins {
    alias(libs.plugins.template.android.library)
}

android {
    namespace = "id.codemockup.template.core.model"
}

dependencies {
    implementation(projects.core.common)
}
