plugins {
    alias(libs.plugins.template.android.library)
    alias(libs.plugins.template.android.hilt)
}

android {
    namespace = "id.codemockup.template.core.network"
    buildFeatures { buildConfig = true }
    productFlavors {
        named("staging") { buildConfigField("String", "BASE_URL", "\"https://staging.example.com/\"") }
        named("production") { buildConfigField("String", "BASE_URL", "\"https://example.com/\"") }
    }
}

// Combined flavor/build-type buckets must exist before dependencies are declared.
configurations {
    maybeCreate("stagingReleaseImplementation")
    maybeCreate("productionReleaseImplementation")
}

dependencies {
    debugImplementation(libs.chucker)
    add("stagingReleaseImplementation", libs.chucker)
    add("productionReleaseImplementation", libs.chucker.no.op)
    testImplementation(libs.mockwebserver)
    api(projects.core.data)
    implementation(projects.core.common)
    implementation(projects.core.datastore)
    api(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.coroutines.android)
}
