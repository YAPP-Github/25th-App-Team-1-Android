package com.yapp.common.navigation.destination

import com.yapp.common.navigation.Routes

sealed class OnboardingDestination(val route: String) {
    data object Route : OnboardingDestination(Routes.Onboarding.ROUTE)
    data object Explain : OnboardingDestination(Routes.Onboarding.EXPLAIN)
    data object AlarmTimeSelection : OnboardingDestination(Routes.Onboarding.ALARM_TIME_SELECTION)
    data object Birthday : OnboardingDestination(Routes.Onboarding.BIRTHDAY)
    data object TimeOfBirth : OnboardingDestination(Routes.Onboarding.TIME_OF_BIRTH)
    data object Name : OnboardingDestination(Routes.Onboarding.NAME)
    data object Gender : OnboardingDestination(Routes.Onboarding.GENDER)
    data object Access : OnboardingDestination(Routes.Onboarding.ACCESS)
    data object Complete1 : OnboardingDestination(Routes.Onboarding.COMPLETE_FIRST)
    data object Complete2 : OnboardingDestination(Routes.Onboarding.COMPLETE_SECOND)

    companion object {
        private val routes = listOf(Explain, AlarmTimeSelection, Birthday, TimeOfBirth, Name, Gender, Access, Complete1, Complete2)

        fun nextRoute(currentStep: Int): String? {
            return routes.getOrNull(currentStep)?.route
        }
    }
}
