package com.yapp.alarm.interaction

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
        startDestination = "${AlarmInteractionDestination.AlarmAction.route}?notificationId={notificationId}&snoozeEnabled={snoozeEnabled}&snoozeInterval={snoozeInterval}&snoozeCount={snoozeCount}",
    ) {
        composable(
            route = "${AlarmInteractionDestination.AlarmAction.route}?notificationId={notificationId}&snoozeEnabled={snoozeEnabled}&snoozeInterval={snoozeInterval}&snoozeCount={snoozeCount}",
            arguments = listOf(
                navArgument("notificationId") { type = NavType.LongType; defaultValue = -1L },
                navArgument("snoozeEnabled") { type = NavType.BoolType; defaultValue = false },
                navArgument("snoozeInterval") { type = NavType.IntType; defaultValue = 5 },
                navArgument("snoozeCount") { type = NavType.IntType; defaultValue = 1 },
            ),
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
