import com.yapp.convention.setNamespace

plugins {
    id("orbit.android.library")
    id("orbit.android.hilt")
}

android {
    setNamespace("core.security")
}

dependencies {
    implementation(projects.core.common)
}
