import java.util.Properties

val hermesProperties = Properties().apply {
    rootProject.file("app.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}
fun stringLiteral(value: String): String = "\"" + value.replace("\\", "\\\\")
    .replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r") + "\""

plugins {
    alias(libs.plugins.ramu.android.library)
    alias(libs.plugins.ramu.android.hilt)
}

android {
    namespace = "id.codemockup.ramu.core.network"
    buildFeatures { buildConfig = true }
    defaultConfig {
        buildConfigField("String", "HERMES_BASE_URL", stringLiteral(hermesProperties.getProperty("HERMES_BASE_URL", "http://10.0.2.2:8642/").trim()))
        buildConfigField("String", "HERMES_API_KEY", "\"\"")
    }
    buildTypes {
        named("debug") {
            buildConfigField("String", "HERMES_API_KEY", stringLiteral(hermesProperties.getProperty("HERMES_API_KEY", "").trim()))
        }
    }
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
