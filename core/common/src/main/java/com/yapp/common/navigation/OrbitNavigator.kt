package com.yapp.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions

class OrbitNavigator(
    private val navController: NavHostController,
) {
    val startDestination = Routes.Onboarding.ROUTE

    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTab: TopLevelDestination?
        @Composable get() = currentDestination
            ?.route
            ?.let(TopLevelDestination.Companion::find)

    fun navigateToTopLevelDestination(tab: TopLevelDestination) {
        val navOptions = navOptions {
            popUpTo(Routes.Home.ROUTE) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (tab) {
            TopLevelDestination.HOME -> navController.navigate(Routes.Home.ROUTE, navOptions)
            TopLevelDestination.MYPAGE -> navController.navigate(Routes.MyPage.ROUTE, navOptions)
        }
    }

    @Composable
    fun shouldShowBottomBar(): Boolean {
        val currentRoute = currentDestination?.route ?: return false
        return currentRoute in TopLevelDestination.entries.map { it.route }
    }
}

@Composable
fun rememberOrbitNavigator(
    navController: NavHostController = rememberNavController(),
): OrbitNavigator = remember(navController) {
    OrbitNavigator(navController)
}
