package com.kms.onboarding.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kms.onboarding.OnboardingAlarmTimeSelectionScreen
import com.kms.onboarding.OnboardingBirthdayScreen
import com.kms.onboarding.OnboardingContract
import com.kms.onboarding.OnboardingExplainScreen

fun NavGraphBuilder.onboardingNavGraph(
    stateProvider: () -> OnboardingContract.State,
    eventDispatcher: (OnboardingContract.Action) -> Unit,
    onFinishOnboarding: () -> Unit,
) {
    composable(OnboardingDestination.Explain.route) {
        OnboardingExplainScreen(
            state = stateProvider(),
            onNextClick = {
                eventDispatcher(OnboardingContract.Action.NextStep)
            },
        )
    }

    composable(OnboardingDestination.AlarmTimeSelection.route) {
        OnboardingAlarmTimeSelectionScreen(
            state = stateProvider(),
            currentStep = 1,
            totalSteps = 2,
            onNextClick = {
                eventDispatcher(OnboardingContract.Action.NextStep)
            },
            onBackClick = {
                eventDispatcher(OnboardingContract.Action.PreviousStep)
            },
        )
    }

    composable(OnboardingDestination.Birthday.route) {
        OnboardingBirthdayScreen(
            state = stateProvider(),
            currentStep = 2,
            totalSteps = 2,
            onNextClick = {
                eventDispatcher(OnboardingContract.Action.NextStep)
            },
            onBackClick = {
                eventDispatcher(OnboardingContract.Action.PreviousStep)
            },
        )
    }
}
