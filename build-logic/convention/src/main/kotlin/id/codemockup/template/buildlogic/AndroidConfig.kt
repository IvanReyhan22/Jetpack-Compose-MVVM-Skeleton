package id.codemockup.template.buildlogic

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

fun Project.configureAndroid(extension: ApplicationExtension) {
    extension.apply {
        compileSdk = 37
        defaultConfig {
            minSdk = 29
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        flavorDimensions += "environment"
        productFlavors {
            create("staging") { dimension = "environment" }
            create("production") { dimension = "environment" }
        }
    }
    dependencies.add("testImplementation", library("junit"))
    dependencies.add("testImplementation", library("coroutines-test"))
}

fun Project.configureAndroid(extension: LibraryExtension) {
    extension.apply {
        compileSdk = 37
        defaultConfig {
            minSdk = 29
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        flavorDimensions += "environment"
        productFlavors {
            create("staging") { dimension = "environment" }
            create("production") { dimension = "environment" }
        }
    }
    dependencies.add("testImplementation", library("junit"))
    dependencies.add("testImplementation", library("coroutines-test"))
}

fun Project.configureCompose(extension: CommonExtension) {
    pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
    extension.buildFeatures.compose = true
    dependencies.add("implementation", dependencies.platform(library("androidx-compose-bom")))
    dependencies.add("implementation", library("androidx-compose-ui"))
    dependencies.add("implementation", library("androidx-compose-material3"))
    dependencies.add("implementation", library("androidx-compose-ui-tooling-preview"))
    dependencies.add("debugImplementation", library("androidx-compose-ui-tooling"))
}
