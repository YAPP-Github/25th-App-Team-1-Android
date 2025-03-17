package com.yapp.analytics

import com.amplitude.android.Amplitude
import javax.inject.Inject

class AmplitudeAnalyticsHelper @Inject constructor(
    private val amplitude: Amplitude,
) : AnalyticsHelper() {
    override fun logEvent(event: String) {
        amplitude.track(event)
    }
}
