package com.yapp.common.navigation.destination

import com.yapp.common.navigation.Routes

sealed class SplashDestination(val route: String) {
    data object Route : SplashDestination(Routes.Splash.ROUTE)
}
