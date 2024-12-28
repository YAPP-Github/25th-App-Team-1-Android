package com.yapp.orbit

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OrbitApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
