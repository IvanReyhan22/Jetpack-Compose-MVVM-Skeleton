import id.codemockup.ramu.buildlogic.library
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("ramu.android.library")
        pluginManager.apply("ramu.android.hilt")
        pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
        listOf("model", "data", "common", "datastore", "extensions", "network", "domain").forEach {
            dependencies.add("implementation", project(":core:$it"))
        }
        dependencies.add("implementation", project(":designsystem"))
        listOf("androidx-navigation-compose", "androidx-hilt-navigation-compose", "androidx-lifecycle-runtime-compose",
            "androidx-lifecycle-viewmodel-ktx", "coroutines-android", "kotlinx-serialization-json").forEach {
            dependencies.add("implementation", library(it))
        }
    }
}
