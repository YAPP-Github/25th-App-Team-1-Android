package com.kms.onboarding.navigation

import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController

@Stable
class OnboardingAppState(
    val navController: NavHostController,
) {
    val startDestination: String = OnboardingDestination.Explain.route

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
}
