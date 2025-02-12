package com.yapp.common.navigation.destination

import com.yapp.common.navigation.Routes

sealed class HomeDestination(val route: String) {
    data object Route : HomeDestination(Routes.Home.ROUTE)
    data object Home : HomeDestination(Routes.Home.HOME)
    data object AlarmAddEdit : HomeDestination(Routes.Home.ALARM_ADD_EDIT)
}
