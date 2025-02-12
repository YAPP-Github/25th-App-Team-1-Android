import com.yapp.convention.setNamespace

plugins {
    id("orbit.android.feature")
}

android {
    setNamespace("feature.navigator")
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.feature.home)
    implementation(projects.feature.onboarding)
    implementation(projects.feature.mission)
    implementation(projects.feature.fortune)
    implementation(projects.feature.setting)
}
