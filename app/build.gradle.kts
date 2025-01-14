plugins {
    id("orbit.android.application")
    id("orbit.android.compose")
}

android {
    namespace = "com.yapp.orbit"

    defaultConfig {
        applicationId = "com.yapp.orbit"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.buildconfig)
    implementation(projects.core.network)
    implementation(projects.core.designsystem)
    implementation(projects.core.datastore)
    implementation(projects.feature.navigator)
}
