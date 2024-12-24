plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

dependencies {
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    compileOnly(libs.compose.compiler.extension)
}

gradlePlugin {
    plugins {
        register("androidHilt") {
            id = "orbit.android.hilt"
            implementationClass = "com.yapp.convention.HiltAndroidPlugin"
        }
        register("kotlinHilt") {
            id = "orbit.kotlin.hilt"
            implementationClass = "com.yapp.convention.HiltKotlinPlugin"
        }
    }
}
