import com.yapp.convention.setNamespace

plugins {
    id("orbit.android.library")
    id("orbit.android.hilt")
}

android {
    setNamespace("core.buildconfig")
}

dependencies {
    implementation(projects.core.common)
}
