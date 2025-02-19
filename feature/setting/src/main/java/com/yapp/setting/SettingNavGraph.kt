package com.yapp.setting

import android.net.Uri
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
import com.yapp.common.navigation.destination.WebViewDestination
import com.yapp.common.navigation.extensions.sharedHiltViewModel
import com.yapp.ui.base.BaseViewModel

fun NavGraphBuilder.settingNavGraph(
    navigator: OrbitNavigator,
) {
    navigation(
        route = SettingDestination.Route.route,
        startDestination = SettingDestination.Setting.route,
    ) {
        SettingDestination.routes.forEach { destination ->
            when (destination) {
                SettingDestination.Setting -> {
                    composable(route = destination.route) { backStackEntry ->
                        val viewModel =
                            backStackEntry.sharedHiltViewModel<SettingViewModel>(navigator.navController)

                        LaunchedEffect(viewModel) {
                            viewModel.container.sideEffectFlow.collect { sideEffect ->
                                handleSettingSideEffect(sideEffect, navigator, viewModel)
                            }
                        }

                        SettingRoute(viewModel)
                    }
                }

                SettingDestination.EditProfile -> {
                    composable(route = destination.route) { backStackEntry ->
                        val viewModel =
                            backStackEntry.sharedHiltViewModel<EditProfileViewModel>(navigator.navController)

                        LaunchedEffect(viewModel) {
                            viewModel.container.sideEffectFlow.collect { sideEffect ->
                                handleSettingSideEffect(sideEffect, navigator, viewModel)
                            }
                        }

                        EditProfileRoute(viewModel)
                    }
                }

                SettingDestination.EditBirthday -> {
                    composable(
                        route = destination.route,
                        enterTransition = {
                            slideInVertically(
                                initialOffsetY = { it },
                                animationSpec = tween(
                                    durationMillis = 350,
                                    easing = FastOutSlowInEasing,
                                ),
                            )
                        },
                        exitTransition = {
                            slideOutVertically(
                                targetOffsetY = { -it },
                                animationSpec = tween(
                                    durationMillis = 250,
                                    easing = FastOutSlowInEasing,
                                ),
                            )
                        },
                        popEnterTransition = {
                            slideInVertically(
                                initialOffsetY = { -it },
                                animationSpec = tween(
                                    durationMillis = 300,
                                    easing = FastOutSlowInEasing,
                                ),
                            )
                        },
                        popExitTransition = {
                            slideOutVertically(
                                targetOffsetY = { it },
                                animationSpec = tween(
                                    durationMillis = 300,
                                    easing = FastOutSlowInEasing,
                                ),
                            )
                        },
                    ) { backStackEntry ->
                        val viewModel =
                            backStackEntry.sharedHiltViewModel<EditProfileViewModel>(navigator.navController)

                        LaunchedEffect(viewModel) {
                            viewModel.container.sideEffectFlow.collect { sideEffect ->
                                handleSettingSideEffect(sideEffect, navigator, viewModel)
                            }
                        }

                        EditBirthdayRoute(viewModel)
                    }
                }

                else -> {}
            }
        }
    }
}

private fun handleSettingSideEffect(
    sideEffect: SettingContract.SideEffect,
    navigator: OrbitNavigator,
    viewModel: BaseViewModel<*, *>,
) {
    when (sideEffect) {
        is SettingContract.SideEffect.Navigate -> navigator.navigateTo(
            route = sideEffect.route,
            popUpTo = sideEffect.popUpTo,
            inclusive = sideEffect.inclusive,
        )

        SettingContract.SideEffect.NavigateBack -> navigator.navigateBack()

        is SettingContract.SideEffect.OpenWebView -> {
            navigator.navigateTo("${WebViewDestination.WebView.route}/${Uri.encode(sideEffect.url)}")
        }
        else -> {}
    }
}
