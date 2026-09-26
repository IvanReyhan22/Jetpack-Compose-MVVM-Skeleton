package id.codemockup.template.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

fun Project.library(alias: String) = extensions.getByType<VersionCatalogsExtension>()
    .named("libs").findLibrary(alias).get()
