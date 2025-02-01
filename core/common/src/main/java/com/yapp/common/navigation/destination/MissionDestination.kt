package com.yapp.common.navigation.destination

import com.yapp.common.navigation.Routes

sealed class MissionDestination(val route: String) {
    data object Route : MissionDestination(Routes.Mission.ROUTE)
    data object Mission : MissionDestination(Routes.Mission.MISSION)
    data object Progress : MissionDestination(Routes.Mission.PROGRESS)

    companion object {
        val routes = listOf(Mission, Progress)
    }
}
