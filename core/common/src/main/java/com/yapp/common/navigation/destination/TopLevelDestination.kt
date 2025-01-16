package com.yapp.common.navigation.destination

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.yapp.common.navigation.Routes

enum class TopLevelDestination(
    @DrawableRes val iconId: Int,
    @StringRes val titleId: Int,
    val route: String,
) {
    HOME(
        iconId = core.designsystem.R.drawable.ic_launcher_foreground,
        titleId = core.designsystem.R.string.app_name,
        route = Routes.Home.ROUTE,
    ),
    MYPAGE(
        iconId = core.designsystem.R.drawable.ic_launcher_foreground,
        titleId = core.designsystem.R.string.app_name,
        route = Routes.MyPage.MYPAGE,
    ),
    ;

    companion object {
        operator fun contains(route: String): Boolean = entries.any { it.route == route }
        fun find(route: String): TopLevelDestination? = entries.find { it.route == route }
    }
}
