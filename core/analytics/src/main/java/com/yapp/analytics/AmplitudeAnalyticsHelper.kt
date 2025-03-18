package com.yapp.analytics

import com.amplitude.android.Amplitude
import com.amplitude.core.events.BaseEvent
import javax.inject.Inject

class AmplitudeAnalyticsHelper @Inject constructor(
    private val amplitude: Amplitude,
) : AnalyticsHelper() {
    override fun logEvent(event: AnalyticsEvent) {
        amplitude.track(event.toAmplitudeEvent())
    }

    private fun AnalyticsEvent.toAmplitudeEvent(): BaseEvent {
        return BaseEvent().apply {
            this.eventType = type
            this.eventProperties = properties?.toMutableMap()
        }
    }
}
