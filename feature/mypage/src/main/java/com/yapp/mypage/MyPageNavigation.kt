package com.yapp.mypage

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

fun NavController.navigateMypage(navOptions: NavOptions) {
    navigate(MypageRoute.MYPAGE, navOptions)
}

fun NavGraphBuilder.mypageNavGraph(
    padding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    composable(route = MypageRoute.MYPAGE) {
        MypageRoute(
            padding = padding,
            modifier = modifier,
        )
    }
}

object MypageRoute {
    const val MYPAGE = "mypage"
}
