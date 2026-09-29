plugins { `kotlin-dsl` }
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin { compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17) } }
dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.compose.gradlePlugin)
    implementation(libs.kotlin.serialization.gradlePlugin)
    implementation(libs.hilt.gradlePlugin)
    implementation(libs.ksp.gradlePlugin)
}
gradlePlugin {
    plugins {
        register("androidApplication") { id = "ramu.android.application"; implementationClass = "AndroidApplicationConventionPlugin" }
        register("androidLibrary") { id = "ramu.android.library"; implementationClass = "AndroidLibraryConventionPlugin" }
        register("applicationCompose") { id = "ramu.android.application.compose"; implementationClass = "AndroidApplicationComposeConventionPlugin" }
        register("libraryCompose") { id = "ramu.android.library.compose"; implementationClass = "AndroidLibraryComposeConventionPlugin" }
        register("hilt") { id = "ramu.android.hilt"; implementationClass = "AndroidHiltConventionPlugin" }
        register("feature") { id = "ramu.android.feature"; implementationClass = "AndroidFeatureConventionPlugin" }
    }
}
