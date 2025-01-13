package com.yapp.mypage

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.mypageNavGraph() {
    composable(route = MypageRoute.MYPAGE) {
        MypageRoute()
    }
}

object MypageRoute {
    const val MYPAGE = "mypage"
}
