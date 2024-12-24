import com.yapp.convention.configureCoroutine
import com.yapp.convention.configureHiltAndroid
import com.yapp.convention.configureKotlinAndroid

plugins {
    id("com.android.library")
}

configureKotlinAndroid()
configureCoroutine()
configureHiltAndroid()
