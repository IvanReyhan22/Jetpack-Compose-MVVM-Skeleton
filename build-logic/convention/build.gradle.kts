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
        register("androidApplication") { id = "template.android.application"; implementationClass = "AndroidApplicationConventionPlugin" }
        register("androidLibrary") { id = "template.android.library"; implementationClass = "AndroidLibraryConventionPlugin" }
        register("applicationCompose") { id = "template.android.application.compose"; implementationClass = "AndroidApplicationComposeConventionPlugin" }
        register("libraryCompose") { id = "template.android.library.compose"; implementationClass = "AndroidLibraryComposeConventionPlugin" }
        register("hilt") { id = "template.android.hilt"; implementationClass = "AndroidHiltConventionPlugin" }
        register("feature") { id = "template.android.feature"; implementationClass = "AndroidFeatureConventionPlugin" }
    }
}
