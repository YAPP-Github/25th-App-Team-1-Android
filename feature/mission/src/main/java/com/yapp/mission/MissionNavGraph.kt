package com.yapp.mission

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.common.navigation.destination.MissionDestination

fun NavGraphBuilder.missionNavGraph(
    navigator: OrbitNavigator,
) {
    navigation(
        route = MissionDestination.Route.route,
        startDestination = MissionDestination.Mission.route,
    ) {
        composable(route = MissionDestination.Mission.route) {
            MissionRoute()
        }
    }
}
