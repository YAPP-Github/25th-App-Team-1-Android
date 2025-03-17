import com.yapp.convention.setNamespace

plugins {
    id("orbit.android.library")
    id("orbit.android.hilt")
    id("orbit.android.compose")
}

android {
    setNamespace("com.yapp.analytics")
}

dependencies {
    implementation(projects.core.common)
    implementation(libs.amplitude.analytics)
}
