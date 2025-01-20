package com.kms.onboarding

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.kms.onboarding.component.OnBoardingTopAppBar
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.component.button.OrbitButton
import com.yapp.ui.utils.heightForScreenPercentage
import feature.onboarding.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun OnboardingAccessRoute(
    navigator: OrbitNavigator,
    viewModel: OnboardingViewModel,
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val permissionState = rememberPermissionState(
        permission = android.Manifest.permission.POST_NOTIFICATIONS,
    )
    val context = LocalContext.current

    LaunchedEffect(permissionState.status) {
        if (permissionState.status.isGranted) {
            Log.d("OnboardingAccessRoute", "Permission granted, navigating to next page")
            viewModel.processAction(OnboardingContract.Action.NextStep)
        }
    }

    OnboardingAccessScreen(
        state = state,
        currentStep = 6,
        totalSteps = 6,
        permissionStatus = permissionState.status,
        onRequestPermission = {
            if (!permissionState.status.isGranted) {
                Log.d("OnboardingAccessRoute", "Launching permission request")
                permissionState.launchPermissionRequest()
            }
        },
        onNavigateToNext = {
            viewModel.processAction(OnboardingContract.Action.NextStep)
        },
        onBackClick = { viewModel.processAction(OnboardingContract.Action.PreviousStep) },
        onNavigateToSettings = {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
        },
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun OnboardingAccessScreen(
    state: OnboardingContract.State,
    currentStep: Int,
    totalSteps: Int,
    permissionStatus: PermissionStatus,
    onRequestPermission: () -> Unit,
    onNavigateToNext: () -> Unit,
    onBackClick: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    val defaultText = stringResource(id = R.string.onboarding_step7_text_default_title)
    val refusalText = stringResource(id = R.string.onboarding_step7_text_refuse_title)

    val (text, imageRes) = remember(permissionStatus) {
        when (permissionStatus) {
            is PermissionStatus.Denied -> {
                if (permissionStatus.shouldShowRationale) {
                    Pair(
                        refusalText,
                        core.designsystem.R.drawable.ic_onboarding_authorization_refusal,
                    )
                } else {
                    Pair(
                        defaultText,
                        core.designsystem.R.drawable.ic_onboarding_authorization_guide,
                    )
                }
            }

            else -> Pair(defaultText, core.designsystem.R.drawable.ic_onboarding_authorization_guide)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OrbitTheme.colors.gray_900)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
    ) {
        OnBoardingTopAppBar(
            currentStep = currentStep,
            totalSteps = totalSteps,
            onBackClick = onBackClick,
            showTopAppBarActions = true,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
        ) {
            Spacer(modifier = Modifier.heightForScreenPercentage(0.05f))

            Text(
                text = text,
                style = OrbitTheme.typography.heading1SemiBold,
                color = OrbitTheme.colors.white,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.heightForScreenPercentage(0.123f))

            if (imageRes != 0) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.CenterHorizontally),
                )
            }
        }

        if (permissionStatus is PermissionStatus.Denied && permissionStatus.shouldShowRationale) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                OrbitButton(
                    label = "나중에 하기",
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    onClick = onNavigateToNext,
                    enabled = true,
                    containerColor = OrbitTheme.colors.gray_600,
                    contentColor = OrbitTheme.colors.white,
                    pressedContainerColor = OrbitTheme.colors.gray_500,
                    pressedContentColor = OrbitTheme.colors.white.copy(alpha = 0.7f),

                )

                OrbitButton(
                    label = "설정으로 가기",
                    onClick = onNavigateToSettings,
                    enabled = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                )
            }
        } else {
            OrbitButton(
                label = "네. 알겠어요",
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 12.dp),
                onClick = onRequestPermission,
                enabled = true,
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
@Preview
fun OnboardingAccessScreenPreview() {
    OrbitTheme {
        OnboardingAccessScreen(
            state = OnboardingContract.State(),
            currentStep = 6,
            totalSteps = 6,
            permissionStatus = PermissionStatus.Denied(shouldShowRationale = true),
            onRequestPermission = {},
            onNavigateToNext = {},
            onBackClick = {},
            onNavigateToSettings = {},
        )
    }
}
