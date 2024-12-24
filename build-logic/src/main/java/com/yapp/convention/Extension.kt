package com.yapp.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.konan.properties.Properties

internal val Project.applicationExtension: CommonExtension<*, *, *, *, *, *>
    get() = extensions.getByType<ApplicationExtension>()

internal val Project.libraryExtension: CommonExtension<*, *, *, *, *, *>
    get() = extensions.getByType<LibraryExtension>()

internal val Project.androidExtension: CommonExtension<*, *, *, *, *, *>
    get() = runCatching { libraryExtension }
        .recoverCatching { applicationExtension }
        .onFailure { println("Could not find Library or Application extension from this project") }
        .getOrThrow()

internal val ExtensionContainer.libs: VersionCatalog
    get() = getByType<VersionCatalogsExtension>().named("libs")

internal fun CommonExtension<*, *, *, *, *, *>.addBuildConfigFields(project: Project) {
    val baseUrl = project.getLocalProperty("baseUrl")
    val isDebug = project.providers.gradleProperty("isDebug").orNull?.toBoolean() ?: false

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
            buildConfigField("boolean", "DEBUG", isDebug.toString())
        }
        getByName("release") {
            buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
            buildConfigField("boolean", "DEBUG", "false")
        }
    }
}

internal fun Project.getLocalProperty(key: String, defaultValue: String? = null): String {
    val propertiesFile = rootProject.file("local.properties")
    if (propertiesFile.exists()) {
        val properties = Properties().apply {
            load(propertiesFile.inputStream())
        }
        return properties.getProperty(key, defaultValue)
    }
    return defaultValue ?: throw IllegalArgumentException("Property $key not found in local.properties and no default value provided")
}
