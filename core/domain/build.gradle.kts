plugins {
    alias(libs.plugins.ramu.android.library)
    alias(libs.plugins.ramu.android.hilt)
}

android {
    namespace = "id.codemockup.ramu.core.domain"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.common)
    implementation(projects.core.network)
    implementation(projects.core.datastore)
    implementation(libs.coroutines.android)
}
