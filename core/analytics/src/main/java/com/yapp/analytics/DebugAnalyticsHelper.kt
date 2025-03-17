package com.yapp.analytics

import android.util.Log

class DebugAnalyticsHelper : AnalyticsHelper() {
    override fun logEvent(event: AnalyticsEvent) {
        Log.d("DebugAnalyticsHelper", "logEvent: $event")
    }
}
