package com.yapp.analytics

import androidx.compose.runtime.staticCompositionLocalOf

abstract class AnalyticsHelper {
    abstract fun logEvent(event: AnalyticsEvent)
}

val LocalAnalyticsHelper = staticCompositionLocalOf<AnalyticsHelper> {
    DebugAnalyticsHelper()
}
