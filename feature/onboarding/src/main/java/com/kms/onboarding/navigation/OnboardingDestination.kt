package com.kms.onboarding.navigation

sealed class OnboardingDestination(val route: String) {
    data object Explain : OnboardingDestination(Routes.EXPLAIN)
    data object AlarmTimeSelection : OnboardingDestination(Routes.ALARM_TIME_SELECTION)
    data object Birthday : OnboardingDestination(Routes.BIRTHDAY)
    data object TimeOfBirth : OnboardingDestination(Routes.TIME_OF_BIRTH)
    data object Name : OnboardingDestination(Routes.NAME)
    data object Gender : OnboardingDestination(Routes.GENDER)

    companion object {
        private val routes = listOf(Explain, AlarmTimeSelection, Birthday, TimeOfBirth, Name, Gender)

        fun nextRoute(currentStep: Int): String? {
            return routes.getOrNull(currentStep)?.route
        }
    }
}
