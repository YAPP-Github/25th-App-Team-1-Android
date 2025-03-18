package com.yapp.analytics

import android.util.Log

class DebugAnalyticsHelper : AnalyticsHelper() {
    private var userId: String = ""

    override fun logEvent(event: AnalyticsEvent) {
        Log.d("DebugAnalyticsHelper", "userId: $userId logEvent: $event")
    }

    override fun setUserId(userId: Long?) {
        this.userId = "Orbit_$userId"
        Log.d("DebugAnalyticsHelper", "setUserId: $userId")
    }
}
