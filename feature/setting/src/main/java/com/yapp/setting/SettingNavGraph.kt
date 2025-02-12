package com.yapp.setting

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.common.navigation.destination.SettingDestination
import com.yapp.common.navigation.extensions.sharedHiltViewModel

fun NavGraphBuilder.settingNavGraph(
    navigator: OrbitNavigator,
) {
    navigation(
        route = SettingDestination.Route.route,
        startDestination = SettingDestination.Setting.route,
    ) {
        SettingDestination.routes.forEach { destination ->
            if (destination == SettingDestination.EditBirthday) {
                composable(
                    route = destination.route,
                    enterTransition = {
                        slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
                        )
                    },
                    exitTransition = {
                        slideOutVertically(
                            targetOffsetY = { -it },
                            animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
                        )
                    },
                    popEnterTransition = {
                        slideInVertically(
                            initialOffsetY = { -it },
                            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                        )
                    },
                    popExitTransition = {
                        slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                        )
                    },
                ) { backStackEntry ->
                    val viewModel = backStackEntry.sharedHiltViewModel<SettingViewModel>(navigator.navController)

                    LaunchedEffect(viewModel) {
                        viewModel.container.sideEffectFlow.collect { sideEffect ->
                            handleFortuneSideEffect(sideEffect, navigator, viewModel)
                        }
                    }

                    EditBirthdayRoute(viewModel)
                }
            } else {
                composable(route = destination.route) { backStackEntry ->
                    val viewModel = backStackEntry.sharedHiltViewModel<SettingViewModel>(navigator.navController)

                    LaunchedEffect(viewModel) {
                        viewModel.container.sideEffectFlow.collect { sideEffect ->
                            handleFortuneSideEffect(sideEffect, navigator, viewModel)
                        }
                    }

                    when (destination) {
                        SettingDestination.Setting -> SettingRoute(viewModel)
                        SettingDestination.EditProfile -> EditProfileRoute(viewModel)
                        else -> {}
                    }
                }
            }
        }
    }
}

private fun handleFortuneSideEffect(
    sideEffect: SettingContract.SideEffect,
    navigator: OrbitNavigator,
    viewModel: SettingViewModel,
) {
    when (sideEffect) {
        is SettingContract.SideEffect.Navigate -> navigator.navigateTo(
            route = sideEffect.route,
            popUpTo = sideEffect.popUpTo,
            inclusive = sideEffect.inclusive,
        )
        SettingContract.SideEffect.NavigateBack -> navigator.navigateBack()
    }
}
