import id.codemockup.template.buildlogic.library
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("template.android.library")
        pluginManager.apply("template.android.hilt")
        pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
        listOf("model", "data", "common", "datastore", "extensions", "designsystem", "network", "domain").forEach {
            dependencies.add("implementation", project(":core:$it"))
        }
        listOf("androidx-navigation-compose", "androidx-hilt-navigation-compose", "androidx-lifecycle-runtime-compose",
            "androidx-lifecycle-viewmodel-ktx", "coroutines-android", "kotlinx-serialization-json").forEach {
            dependencies.add("implementation", library(it))
        }
    }
}
