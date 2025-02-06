package com.yapp.common.navigation.destination

import com.yapp.common.navigation.Routes

sealed class FortuneDestination(val route: String) {
    data object Route : FortuneDestination(Routes.Fortune.ROUTE)
    data object Fortune : FortuneDestination(Routes.Fortune.FORTUNE)
    data object Reward : FortuneDestination(Routes.Fortune.REWARD)

    companion object {
        val routes = listOf(Fortune, Reward)
    }
}
