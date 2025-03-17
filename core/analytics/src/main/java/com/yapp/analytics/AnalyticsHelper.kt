package com.yapp.analytics

import androidx.compose.runtime.staticCompositionLocalOf

abstract class AnalyticsHelper {
    abstract fun logEvent(event: AnalyticsEvent)
    abstract fun setUserId(userId: String?)
}

val LocalAnalyticsHelper = staticCompositionLocalOf<AnalyticsHelper> {
    DebugAnalyticsHelper()
}
