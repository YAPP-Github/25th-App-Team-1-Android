package com.yapp.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.yapp.alarm.AlarmAddEditRoute
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.common.navigation.destination.HomeDestination

fun NavGraphBuilder.homeNavGraph(
    navigator: OrbitNavigator,
) {
    navigation(
        route = HomeDestination.Route.route,
        startDestination = HomeDestination.Home.route,
    ) {
        composable(route = HomeDestination.Home.route) {
            HomeRoute()
        }

        composable(route = HomeDestination.AlarmAddEdit.route) {
            AlarmAddEditRoute(
                navigator = navigator,
            )
        }
    }
}
