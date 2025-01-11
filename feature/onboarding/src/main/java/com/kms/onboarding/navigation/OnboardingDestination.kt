package com.kms.onboarding.navigation

sealed class OnboardingDestination(val route: String) {
    data object Explain : OnboardingDestination(Routes.EXPLAIN)
    data object AlarmTimeSelection : OnboardingDestination(Routes.ALARM_TIME_SELECTION)
    data object Birthday : OnboardingDestination(Routes.BIRTHDAY)

    companion object {
        private val routes = listOf(Explain, AlarmTimeSelection, Birthday)

        fun nextRoute(currentStep: Int): String? {
            return routes.getOrNull(currentStep)?.route
        }
    }
}
