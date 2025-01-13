package com.yapp.navigator.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.yapp.home.HomeRoute
import com.yapp.mypage.MypageRoute

internal class MainNavigator(
    val navController: NavHostController,
) {
    val startDestination = HomeRoute.ALARM_ADD_EDIT
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTab: MainNavTab?
        @Composable get() = currentDestination
            ?.route
            ?.let(MainNavTab.Companion::find)

    fun navigate(tab: MainNavTab) {
        val navOptions = navOptions {
            popUpTo(HomeRoute.HOME) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (tab) {
            MainNavTab.HOME -> navController.navigate(HomeRoute.HOME, navOptions)
            MainNavTab.MYPAGE -> navController.navigate(MypageRoute.MYPAGE, navOptions)
        }
    }

    @Composable
    fun shouldShowBottomBar(): Boolean {
        val currentRoute = currentDestination?.route ?: return false
        return currentRoute in MainNavTab.entries.map { it.route }
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}
