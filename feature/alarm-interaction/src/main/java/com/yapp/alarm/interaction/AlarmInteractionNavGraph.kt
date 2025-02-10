package com.yapp.alarm.interaction

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yapp.alarm.interaction.action.AlarmActionRoute
import com.yapp.alarm.interaction.snooze.AlarmSnoozeTimerRoute
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.common.navigation.destination.AlarmInteractionDestination

fun NavGraphBuilder.alarmInteractionNavGraph(
    navigator: OrbitNavigator,
) {
    navigation(
        route = AlarmInteractionDestination.Route.route,
        startDestination = AlarmInteractionDestination.AlarmAction.route,
    ) {
        composable(
            route = AlarmInteractionDestination.AlarmAction.route,
        ) {
            AlarmActionRoute(
                navigator = navigator,
            )
        }

        composable(
            route = AlarmInteractionDestination.AlarmSnoozeTimer.route,
        ) {
            AlarmSnoozeTimerRoute(
                navigator = navigator,
            )
        }
    }
}
