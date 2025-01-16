package com.yapp.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.yapp.alarm.AlarmAddEditRoute
import com.yapp.common.navigation.Routes

fun NavGraphBuilder.homeNavGraph() {
    navigation(
        route = Routes.Home.ROUTE,
        startDestination = Routes.Home.HOME,
    ) {
        composable(route = Routes.Home.HOME) {
            HomeRoute()
        }

        composable(route = Routes.Home.ALARM_ADD_EDIT) {
            AlarmAddEditRoute()
        }
    }
}
