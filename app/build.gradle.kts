plugins {
    id("orbit.android.application")
    id("orbit.android.compose")
    alias(libs.plugins.google.service)
    alias(libs.plugins.firebase.app.distribution)
}

android {
    namespace = "com.yapp.orbit"

    defaultConfig {
        versionCode = 2
        versionName = "1.0.0"
        targetSdk = 34
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
    implementation(projects.core.alarm)
    implementation(projects.core.media)
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.feature.onboarding)
    implementation(projects.feature.home)
    implementation(projects.feature.alarmInteraction)
    implementation(projects.feature.fortune)
    implementation(projects.feature.mission)
    implementation(projects.feature.setting)
    implementation(projects.feature.navigator)
}
