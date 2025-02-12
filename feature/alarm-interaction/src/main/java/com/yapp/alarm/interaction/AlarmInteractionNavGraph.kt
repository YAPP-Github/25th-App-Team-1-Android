package com.yapp.alarm.interaction

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.gson.Gson
import com.yapp.alarm.interaction.action.AlarmActionRoute
import com.yapp.alarm.interaction.snooze.AlarmSnoozeTimerRoute
import com.yapp.common.navigation.JsonNavType
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.common.navigation.destination.AlarmInteractionDestination
import com.yapp.domain.model.Alarm

class AlarmArgType : JsonNavType<Alarm>() {
    override fun fromJsonParse(value: String): Alarm {
        return Gson().fromJson(value, Alarm::class.java)
    }

    override fun Alarm.getJsonParse(): String {
        return Gson().toJson(this)
    }
}

fun NavGraphBuilder.alarmInteractionNavGraph(
    navigator: OrbitNavigator,
) {
    navigation(
        route = AlarmInteractionDestination.Route.route,
        startDestination = "${AlarmInteractionDestination.AlarmAction.route}/{alarm}",
    ) {
        composable(
            route = "${AlarmInteractionDestination.AlarmAction.route}/{alarm}",
            arguments = listOf(
                navArgument("alarm") {
                    type = AlarmArgType()
                    defaultValue = Alarm()
                },
            ),
        ) {
            AlarmActionRoute(
                navigator = navigator,
            )
        }

        composable(
            route = "${AlarmInteractionDestination.AlarmSnoozeTimer.route}/{alarm}",
            arguments = listOf(
                navArgument("alarm") {
                    type = AlarmArgType()
                    defaultValue = Alarm()
                },
            ),
        ) {
            AlarmSnoozeTimerRoute(
                navigator = navigator,
            )
        }
    }
}
