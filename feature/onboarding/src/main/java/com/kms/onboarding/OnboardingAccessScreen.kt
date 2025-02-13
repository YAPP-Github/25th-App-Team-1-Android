package com.kms.onboarding

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.kms.onboarding.component.OnBoardingTopAppBar
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.component.button.OrbitButton
import com.yapp.ui.utils.heightForScreenPercentage
import feature.onboarding.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun OnboardingAccessRoute(
    navigator: OrbitNavigator,
    viewModel: OnboardingViewModel,
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current

    val permissionState =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            rememberPermissionState(permission = android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            null
        }

    var hasRequestedPermission by remember { mutableStateOf(false) }
    var hasCheckedNotification by remember { mutableStateOf(false) }
    var hasCheckedAlarm by remember { mutableStateOf(false) }

    var isAlarmPermissionGranted by remember { mutableStateOf(false) }

    var hasDelayed by remember { mutableStateOf(false) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isAlarmPermissionGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
                    alarmManager.canScheduleExactAlarms()
                } else {
                    true
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(permissionState?.status, isAlarmPermissionGranted) {
        if (!hasDelayed) {
            delay(1000)
            hasDelayed = true
        }

        val isNotificationGranted = permissionState?.status?.isGranted ?: true

        if (!isNotificationGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasCheckedNotification) {
            hasCheckedNotification = true
            permissionState?.launchPermissionRequest()
        } else if (!isAlarmPermissionGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !hasCheckedAlarm) {
            hasCheckedAlarm = true
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
        }

        if ((hasCheckedNotification || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) &&
            (hasCheckedAlarm || Build.VERSION.SDK_INT < Build.VERSION_CODES.S)
        ) {
            hasRequestedPermission = true
        }

        if (isNotificationGranted && isAlarmPermissionGranted) {
            viewModel.processAction(OnboardingContract.Action.NextStep)
        }
    }

    OnboardingAccessScreen(
        state = state,
        currentStep = 6,
        totalSteps = 6,
        notificationPermissionStatus = permissionState?.status ?: PermissionStatus.Granted,
        isAlarmPermissionGranted = isAlarmPermissionGranted,
        hasRequestedPermission = hasRequestedPermission,
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
    notificationPermissionStatus: PermissionStatus,
    isAlarmPermissionGranted: Boolean,
    hasRequestedPermission: Boolean,
    onNavigateToNext: () -> Unit,
    onBackClick: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    val defaultText = stringResource(id = R.string.onboarding_step7_text_default_title)
    val refusalText = stringResource(id = R.string.onboarding_step7_text_refuse_title)

    val isNotificationDenied = notificationPermissionStatus is PermissionStatus.Denied
    val shouldShowRationale = isNotificationDenied && notificationPermissionStatus.shouldShowRationale
    val isAllPermissionsGranted = !isNotificationDenied && isAlarmPermissionGranted

    val (text, imageRes) = remember(isNotificationDenied, isAlarmPermissionGranted, hasRequestedPermission) {
        when {
            !hasRequestedPermission -> Pair(
                defaultText,
                core.designsystem.R.drawable.ic_onboarding_authorization_guide,
            )
            isAllPermissionsGranted -> Pair(
                defaultText,
                core.designsystem.R.drawable.ic_onboarding_authorization_guide,
            )
            shouldShowRationale || !isAlarmPermissionGranted -> Pair(
                refusalText,
                core.designsystem.R.drawable.ic_onboarding_authorization_refusal,
            )
            else -> Pair(
                defaultText,
                core.designsystem.R.drawable.ic_onboarding_authorization_guide,
            )
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
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }
        }

        if (hasRequestedPermission) {
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
            notificationPermissionStatus = PermissionStatus.Denied(shouldShowRationale = true),
            isAlarmPermissionGranted = false,
            hasRequestedPermission = false,
            onNavigateToNext = {},
            onBackClick = {},
            onNavigateToSettings = {},
        )
    }
}
