plugins {
    alias(libs.plugins.ramu.android.library)
    alias(libs.plugins.ramu.android.hilt)
}

android {
    namespace = "id.codemockup.ramu.core.datastore"
}

dependencies {
    api(projects.core.model)
    implementation(projects.core.common)
    api(libs.coroutines.android)
    implementation(libs.datastore.preferences)
}
