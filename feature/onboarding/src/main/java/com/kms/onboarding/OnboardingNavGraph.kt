package com.kms.onboarding

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.common.navigation.destination.OnboardingDestination
import com.yapp.common.navigation.extensions.sharedViewModel

fun NavGraphBuilder.onboardingNavGraph(
    navigator: OrbitNavigator,
    onFinishOnboarding: () -> Unit,
) {
    navigation(
        route = OnboardingDestination.Route.route,
        startDestination = OnboardingDestination.Explain.route,
    ) {
        OnboardingDestination.routes.forEach { destination ->
            composable(destination.route) { backStackEntry ->
                val viewModel = backStackEntry.sharedViewModel<OnboardingViewModel>(navigator.navController)

                LaunchedEffect(viewModel) {
                    viewModel.container.sideEffectFlow.collect { sideEffect ->
                        handleSideEffect(sideEffect, navigator, viewModel, onFinishOnboarding)
                    }
                }

                when (destination) {
                    OnboardingDestination.Route, OnboardingDestination.Explain -> {
                        OnboardingExplainRoute(viewModel)
                    }
                    OnboardingDestination.AlarmTimeSelection -> {
                        OnboardingAlarmTimeSelectionRoute(viewModel)
                    }
                    OnboardingDestination.Birthday -> {
                        OnboardingBirthdayRoute(viewModel)
                    }
                    OnboardingDestination.TimeOfBirth -> {
                        OnboardingTimeOfBirthRoute(viewModel)
                    }
                    OnboardingDestination.Name -> {
                        OnboardingNameRoute(viewModel)
                    }
                    OnboardingDestination.Gender -> {
                        OnboardingGenderRoute(viewModel)
                    }
                    OnboardingDestination.Access -> {
                        OnboardingAccessRoute(navigator, viewModel)
                    }
                    OnboardingDestination.Complete1 -> {
                        OnboardingCompleteRoute(viewModel)
                    }
                    OnboardingDestination.Complete2 -> {
                        OnboardingCompleteRoute2(viewModel, onFinishOnboarding)
                    }
                }
            }
        }
    }
}

private fun handleSideEffect(
    sideEffect: OnboardingContract.SideEffect,
    navigator: OrbitNavigator,
    viewModel: OnboardingViewModel,
    onFinishOnboarding: () -> Unit,
) {
    when (sideEffect) {
        is OnboardingContract.SideEffect.Navigate -> navigator.navigateTo(
            route = sideEffect.route,
            popUpTo = sideEffect.popUpTo,
            inclusive = sideEffect.inclusive,
        )

        OnboardingContract.SideEffect.NavigateBack -> navigator.navigateBack()
        OnboardingContract.SideEffect.OnboardingCompleted -> onFinishOnboarding()
        OnboardingContract.SideEffect.ResetField -> viewModel.processAction(
            OnboardingContract.Action.Reset,
        )
    }
}
