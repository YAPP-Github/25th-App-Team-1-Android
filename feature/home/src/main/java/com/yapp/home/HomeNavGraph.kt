package com.yapp.home

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.yapp.alarm.AlarmAddEditRoute
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.common.navigation.destination.HomeDestination

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

        composable(route = HomeDestination.AlarmAddEdit.route) {
            AlarmAddEditRoute(
                navigator = navigator,
            )
        }
    }
}
