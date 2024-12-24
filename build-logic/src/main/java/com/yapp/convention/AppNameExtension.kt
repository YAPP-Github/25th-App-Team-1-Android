package com.yapp.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import com.android.build.api.dsl.LibraryExtension

fun Project.setNamespace(namespace: String) {
    extensions.getByType<LibraryExtension>().namespace = namespace
}
