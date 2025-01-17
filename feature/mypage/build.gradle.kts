import com.yapp.convention.setNamespace

plugins {
    id("orbit.android.feature")
}

android {
    setNamespace("feature.myapge")
}

dependencies {
    implementation(projects.core.common)
}
