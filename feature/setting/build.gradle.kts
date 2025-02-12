import com.yapp.convention.setNamespace

plugins {
    id("orbit.android.feature")
}

android {
    setNamespace("feature.setting")
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.common)
    implementation(projects.domain)
    implementation(projects.core.datastore)
    implementation(libs.orbit.core)
    implementation(libs.orbit.compose)
    implementation(libs.orbit.viewmodel)
}
