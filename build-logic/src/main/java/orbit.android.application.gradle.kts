import com.yapp.convention.configureHiltAndroid
import com.yapp.convention.configureKotlinAndroid

plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()
