package com.yapp.analytics

import android.util.Log

class DebugAnalyticsHelper : AnalyticsHelper() {
    override fun logEvent(event: String) {
        Log.d("DebugAnalyticsHelper", "logEvent: $event")
    }
}
