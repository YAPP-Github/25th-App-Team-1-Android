package com.yapp.home

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.yapp.alarm.addedit.AlarmAddEditRoute
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.common.navigation.destination.HomeDestination

const val ADD_ALARM_RESULT_KEY = "addAlarmResult"
const val UPDATE_ALARM_RESULT_KEY = "updateAlarmResult"
const val DELETE_ALARM_RESULT_KEY = "deleteAlarmResult"

fun NavGraphBuilder.homeNavGraph(
    navigator: OrbitNavigator,
    snackBarHostState: SnackbarHostState,
) {
    navigation(
        route = HomeDestination.Route.route,
        startDestination = HomeDestination.Home.route,
    ) {
        composable(route = HomeDestination.Home.route) {
            HomeRoute(
                navigator = navigator,
                snackBarHostState = snackBarHostState,
            )
        }

        composable(
            route = "${HomeDestination.AlarmAddEdit.route}?id={alarmId}",
            arguments = listOf(
                navArgument("alarmId") {
                    type = NavType.LongType
                    defaultValue = -1
                },
            ),
        ) {
            AlarmAddEditRoute(
                navigator = navigator,
                snackBarHostState = snackBarHostState,
            )
        }
    }
}
