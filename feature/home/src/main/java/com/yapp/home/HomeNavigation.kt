package com.yapp.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.yapp.alarm.AlarmAddEditRoute

fun NavGraphBuilder.homeNavGraph() {
    composable(route = HomeRoute.HOME) {
        HomeRoute()
    }

    composable(route = HomeRoute.ALARM_ADD_EDIT) {
        AlarmAddEditRoute()
    }
}

object HomeRoute {
    const val HOME = "home"

    const val ALARM_ADD_EDIT = "alarm_add_edit"
}
