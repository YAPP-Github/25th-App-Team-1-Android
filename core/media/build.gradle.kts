import com.yapp.convention.setNamespace

plugins {
    id("orbit.android.library")
    id("orbit.android.hilt")
    id("kotlinx-serialization")
}

android {
    setNamespace("core.media")
}
