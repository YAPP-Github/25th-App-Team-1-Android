package com.yapp.analytics

import androidx.compose.runtime.staticCompositionLocalOf

abstract class AnalyticsHelper {
    abstract fun logEvent(event: String)
}

val LocalAnalyticsHelper = staticCompositionLocalOf<AnalyticsHelper> {
    DebugAnalyticsHelper()
}
