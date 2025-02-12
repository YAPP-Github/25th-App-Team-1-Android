package com.yapp.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.yapp.common.navigation.destination.HomeDestination
import com.yapp.common.navigation.destination.TopLevelDestination

class OrbitNavigator(
    val navController: NavHostController,
) {
    val startDestination = HomeDestination.Route.route

    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTab: TopLevelDestination?
        @Composable get() = currentDestination
            ?.route
            ?.let(TopLevelDestination.Companion::find)

    fun navigateTo(route: String, popUpTo: String? = null, inclusive: Boolean = false) {
        navController.navigate(route) {
            popUpTo?.let {
                popUpTo(it) { this.inclusive = inclusive }
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateBack() {
        navController.popBackStack()
    }

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
    fun shouldHaveNavigationBarsPadding(): Boolean {
        val currentRoute = currentDestination?.route ?: return false
        return currentRoute !in HomeDestination.Home.route
    }
}

@Composable
fun rememberOrbitNavigator(
    navController: NavHostController = rememberNavController(),
): OrbitNavigator = remember(navController) {
    OrbitNavigator(navController)
}
