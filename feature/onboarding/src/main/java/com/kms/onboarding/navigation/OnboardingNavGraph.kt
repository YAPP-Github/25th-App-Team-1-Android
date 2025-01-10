package com.kms.onboarding.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kms.onboarding.AlarmTimeSelectionScreen
import com.kms.onboarding.BirthdayScreen
import com.kms.onboarding.ExplainScreen
import com.kms.onboarding.OnboardingContract

fun NavGraphBuilder.onboardingNavGraph(
    stateProvider: () -> OnboardingContract.State,
    eventDispatcher: (OnboardingContract.Action) -> Unit,
    onFinishOnboarding: () -> Unit,
) {
    composable(OnboardingDestination.Explain.route) {
        ExplainScreen(
            state = stateProvider(),
            onNextClick = {
                eventDispatcher(OnboardingContract.Action.NextStep)
            },
        )
    }

    composable(OnboardingDestination.AlarmTimeSelection.route) {
        AlarmTimeSelectionScreen(
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
        BirthdayScreen(
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
