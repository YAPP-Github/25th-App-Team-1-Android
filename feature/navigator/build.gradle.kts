import com.yapp.convention.setNamespace

plugins {
    id("orbit.android.feature")
}

android {
    setNamespace("feature.navigator")
}

dependencies {
    implementation(projects.feature.home)
    implementation(projects.feature.mypage)
    implementation(projects.feature.onboarding)
}
