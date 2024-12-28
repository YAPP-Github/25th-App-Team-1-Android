import com.yapp.convention.setNamespace

plugins {
    id("orbit.android.library")
    id("orbit.android.hilt")
    id("kotlinx-serialization")
}

android {
    setNamespace("core.datastore")
}

dependencies {
    implementation(projects.core.common)
    implementation(libs.androidx.datastore)
    implementation(libs.kotlinx.serialization.json)
}
