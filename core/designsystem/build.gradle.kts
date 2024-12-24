import com.yapp.convention.setNamespace

plugins {
    id("orbit.android.library")
    id("orbit.android.compose")
}

android {
    setNamespace("core.designsystem")
}

dependencies {
    implementation(projects.core.ui)
}
