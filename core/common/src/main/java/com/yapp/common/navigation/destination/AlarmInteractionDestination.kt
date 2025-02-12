package com.yapp.common.navigation.destination

import com.yapp.common.navigation.Routes

sealed class AlarmInteractionDestination(val route: String) {
    data object Route : AlarmInteractionDestination(Routes.AlarmInteraction.ROUTE)
    data object AlarmAction : HomeDestination(Routes.AlarmInteraction.ALARM_ACTION)
    data object AlarmSnoozeTimer : HomeDestination(Routes.AlarmInteraction.ALARM_SNOOZE_TIMER)
}
