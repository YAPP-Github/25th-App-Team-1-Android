package com.yapp.navigator.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.yapp.home.HomeRoute
import com.yapp.mypage.MypageRoute

enum class MainNavTab(
    @DrawableRes val iconId: Int,
    @StringRes val titleId: Int,
    val route: String,
) {
    HOME(
        iconId = core.designsystem.R.drawable.ic_launcher_foreground,
        titleId = core.designsystem.R.string.app_name,
        route = HomeRoute.HOME,
    ),
    MYPAGE(
        iconId = core.designsystem.R.drawable.ic_launcher_foreground,
        titleId = core.designsystem.R.string.app_name,
        route = MypageRoute.MYPAGE,
    ),
    ;

    companion object {
        operator fun contains(route: String): Boolean = entries.any { it.route == route }
        fun find(route: String): MainNavTab? = entries.find { it.route == route }
    }
}
