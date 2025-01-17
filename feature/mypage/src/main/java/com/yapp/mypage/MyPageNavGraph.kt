package com.yapp.mypage

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.common.navigation.destination.MyPageDestination

fun NavGraphBuilder.myPageNavGraph(
    navigator: OrbitNavigator,
) {
    navigation(
        route = MyPageDestination.Route.route,
        startDestination = MyPageDestination.MyPage.route,
    ) {
        composable(route = MyPageDestination.MyPage.route) {
            MypageRoute()
        }
    }
}
