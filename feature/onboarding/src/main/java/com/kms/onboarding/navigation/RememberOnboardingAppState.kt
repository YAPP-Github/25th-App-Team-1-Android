package com.kms.onboarding.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberOnboardingAppState(
    navController: NavHostController = rememberNavController(),
): OnboardingAppState {
    return remember { OnboardingAppState(navController) }
}
