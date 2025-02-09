package com.yapp.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.common.navigation.OrbitNavigator
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.home.component.bottomsheet.AlarmListBottomSheet
import com.yapp.ui.component.dialog.OrbitDialog
import com.yapp.ui.component.lottie.LottieAnimation
import com.yapp.ui.component.snackbar.showCustomSnackBar
import com.yapp.ui.lifecycle.LaunchedEffectWithLifecycle
import com.yapp.ui.utils.heightForScreenPercentage
import com.yapp.ui.utils.toPx
import feature.home.R

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    navigator: OrbitNavigator,
    snackBarHostState: SnackbarHostState,
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val sideEffect = viewModel.container.sideEffectFlow

    LaunchedEffect(navigator.navController.currentBackStackEntry?.savedStateHandle?.get<Long>(ADD_ALARM_RESULT_KEY)) {
        navigator.navController.currentBackStackEntry
            ?.savedStateHandle
            ?.get<Long>(ADD_ALARM_RESULT_KEY)
            ?.let { id ->
                viewModel.scrollToAddedAlarm(id)
                navigator.navController.currentBackStackEntry?.savedStateHandle?.remove<Long>(ADD_ALARM_RESULT_KEY)
            }
    }

    LaunchedEffect(navigator.navController.currentBackStackEntry?.savedStateHandle?.get<Long>(UPDATE_ALARM_RESULT_KEY)) {
        navigator.navController.currentBackStackEntry
            ?.savedStateHandle
            ?.get<Long>(UPDATE_ALARM_RESULT_KEY)
            ?.let { id ->
                viewModel.scrollToUpdatedAlarm(id)
                navigator.navController.currentBackStackEntry?.savedStateHandle?.remove<Long>(UPDATE_ALARM_RESULT_KEY)
            }
    }

    LaunchedEffect(navigator.navController.currentBackStackEntry?.savedStateHandle?.get<Long>(DELETE_ALARM_RESULT_KEY)) {
        navigator.navController.currentBackStackEntry
            ?.savedStateHandle
            ?.get<Long>(DELETE_ALARM_RESULT_KEY)
            ?.let { id ->
                viewModel.processAction(HomeContract.Action.DeleteSingleAlarm(id))
                navigator.navController.currentBackStackEntry?.savedStateHandle?.remove<Long>(DELETE_ALARM_RESULT_KEY)
            }
    }

    LaunchedEffectWithLifecycle(sideEffect) {
        sideEffect.collect { effect ->
            when (effect) {
                is HomeContract.SideEffect.NavigateBack -> {
                    navigator.navigateBack()
                }
                is HomeContract.SideEffect.Navigate -> {
                    navigator.navigateTo(
                        route = effect.route,
                        popUpTo = effect.popUpTo,
                        inclusive = effect.inclusive,
                    )
                }
                is HomeContract.SideEffect.ShowSnackBar -> {
                    val result = showCustomSnackBar(
                        snackBarHostState = snackBarHostState,
                        message = effect.message,
                        actionLabel = effect.label,
                        iconRes = effect.iconRes,
                        bottomPadding = effect.bottomPadding,
                        duration = effect.duration,
                    )

                    when (result) {
                        SnackbarResult.ActionPerformed -> effect.onAction()
                        SnackbarResult.Dismissed -> effect.onDismiss()
                    }
                }
            }
        }
    }

    HomeScreen(
        stateProvider = { state },
        eventDispatcher = viewModel::processAction,
    )
}

@Composable
fun HomeScreen(
    stateProvider: () -> HomeContract.State,
    eventDispatcher: (HomeContract.Action) -> Unit,
) {
    val state = stateProvider()

    if (state.initialLoading) {
        HomeLoadingScreen()
    } else if (state.alarms.isEmpty()) {
        HomeAlarmEmptyScreen(
            onSettingClick = { },
            onMailClick = { },
            onAddClick = { },
        )
    } else {
        HomeContent(
            state = state,
            eventDispatcher = eventDispatcher,
        )
    }
}

@Composable
private fun HomeLoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1F3B64)),
    ) {
        HillWithGradient()

        SkyImage()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color(0xFF17191F).copy(alpha = 0.8f),
                ),
        )

        LottieAnimation(
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.Center),
            resId = core.designsystem.R.raw.star_loading,
        )
    }
}

@Composable
private fun HomeContent(
    state: HomeContract.State,
    eventDispatcher: (HomeContract.Action) -> Unit,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    var sheetHalfExpandHeight by remember { mutableStateOf(0.dp) }

    val listState = rememberLazyListState()

    LaunchedEffect(state.lastAddedAlarmIndex) {
        state.lastAddedAlarmIndex?.let { index ->
            listState.scrollToItem(index)
            eventDispatcher(HomeContract.Action.ResetLastAddedAlarmIndex)
        }
    }

    Box {
        AlarmListBottomSheet(
            alarms = state.alarms,
            menuExpanded = state.dropdownMenuExpanded,
            isAllSelected = state.isAllSelected,
            isSelectionMode = state.isSelectionMode,
            selectedAlarmIds = state.selectedAlarmIds,
            halfExpandedHeight = sheetHalfExpandHeight,
            isLoading = false,
            hasMoreData = false,
            listState = listState,
            onClickAlarm = { alarmId ->
                eventDispatcher(HomeContract.Action.EditAlarm(alarmId))
            },
            onClickAdd = {
                eventDispatcher(HomeContract.Action.NavigateToAlarmCreation)
            },
            onClickMore = {
                eventDispatcher(HomeContract.Action.ToggleDropdownMenuVisibility)
            },
            onClickCheckAll = {
                eventDispatcher(HomeContract.Action.ToggleAllAlarmSelection)
            },
            onClickClose = {
                eventDispatcher(HomeContract.Action.ToggleMultiSelectionMode)
            },
            onClickEdit = {
                eventDispatcher(HomeContract.Action.ToggleMultiSelectionMode)
            },
            onDismissRequest = {
                eventDispatcher(HomeContract.Action.ToggleDropdownMenuVisibility)
            },
            onToggleSelect = { alarmId ->
                eventDispatcher(HomeContract.Action.ToggleAlarmSelection(alarmId))
            },
            onToggleActive = { alarmId ->
                eventDispatcher(HomeContract.Action.ToggleAlarmActivation(alarmId))
            },
            onLoadMore = {
                eventDispatcher(HomeContract.Action.LoadMoreAlarms)
            },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1F3B64)),
            ) {
                HillWithGradient()

                SkyImage()

                val characterY = (screenHeight * 0.28f) - 130.dp

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            sheetHalfExpandHeight = screenHeight - placeable.height.toDp()
                            layout(placeable.width, placeable.height) {
                                placeable.placeRelative(0, 0)
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(characterY))

                    HomeCharacterAnimation(
                        fortuneScore = state.lastFortuneScore,
                        hasActivatedAlarm = state.hasActivatedAlarm,
                        eventDispatcher = eventDispatcher,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HomeFortuneDescription(
                        fortuneScore = state.lastFortuneScore,
                        hasActivatedAlarm = state.hasActivatedAlarm,
                        name = state.name,
                        deliveryTime = state.deliveryTime,
                    )
                }

                HomeTopBar(
                    isTitleVisible = false,
                    onSettingClick = { },
                    onMailClick = { },
                )
            }
        }

        if (state.isSelectionMode && state.selectedAlarmIds.isNotEmpty()) {
            DeleteAlarmButton(
                modifier = Modifier
                    .widthIn(min = 130.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 26.dp),
                selectedAlarmCount = state.selectedAlarmIds.size,
                onClick = {
                    eventDispatcher(HomeContract.Action.ShowDeleteDialog)
                },
            )
        }

        BottomGradient(modifier = Modifier.align(Alignment.BottomCenter))
    }

    if (state.isDeleteDialogVisible) {
        OrbitDialog(
            title = stringResource(id = R.string.alarm_delete_dialog_title),
            message = stringResource(id = R.string.alarm_delete_dialog_message),
            confirmText = stringResource(id = R.string.alarm_delete_dialog_btn_delete),
            cancelText = stringResource(id = R.string.alarm_delete_dialog_btn_cancel),
            onConfirm = {
                eventDispatcher(HomeContract.Action.ConfirmDeletion)
            },
            onCancel = {
                eventDispatcher(HomeContract.Action.HideDeleteDialog)
            },
        )
    }

    if (state.isNoActivatedAlarmDialogVisible) {
        OrbitDialog(
            title = stringResource(id = R.string.no_active_alarm_dialog_title),
            message = stringResource(id = R.string.no_active_alarm_dialog_message),
            confirmText = stringResource(id = R.string.no_active_alarm_dialog_btn_confirm),
            cancelText = stringResource(id = R.string.no_active_alarm_dialog_btn_cancel),
            onConfirm = {
                eventDispatcher(HomeContract.Action.HideNoActivatedAlarmDialog)
            },
            onCancel = {
                eventDispatcher(HomeContract.Action.RollbackPendingAlarmToggle)
            },
        )
    }
}

@Composable
private fun HomeTopBar(
    isTitleVisible: Boolean = true,
    onSettingClick: () -> Unit,
    onMailClick: () -> Unit,
) {
    Box(
        modifier = Modifier.statusBarsPadding(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isTitleVisible) {
                Text(
                    text = stringResource(id = R.string.home_top_bar_title),
                    style = OrbitTheme.typography.heading1SemiBold.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = OrbitTheme.colors.main,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable {
                        onMailClick()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = core.designsystem.R.drawable.ic_mail),
                    contentDescription = "Mail",
                    tint = OrbitTheme.colors.white,
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable {
                        onSettingClick()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = core.designsystem.R.drawable.ic_setting),
                    contentDescription = "Setting",
                    tint = OrbitTheme.colors.white,
                )
            }
        }
    }
}

@Composable
fun HillWithGradient() {
    val hillTopY = (LocalConfiguration.current.screenHeightDp.dp * 0.28f).toPx()

    Canvas(
        modifier = Modifier.fillMaxSize(),
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val circleRadius = canvasWidth / 0.8f

        val gradientBrush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF172E51),
                Color(0xFF184385),
            ),
            startY = hillTopY,
            endY = canvasHeight,
        )

        drawRoundRect(
            brush = gradientBrush,
            topLeft = Offset(0f, hillTopY + circleRadius),
            size = Size(canvasWidth, canvasHeight - hillTopY),
            cornerRadius = CornerRadius(0f, 0f),
        )

        drawCircle(
            brush = gradientBrush,
            radius = circleRadius,
            center = Offset(canvasWidth / 2, hillTopY + circleRadius),
        )
    }
}

@Composable
fun SkyImage() {
    Image(
        painter = painterResource(id = core.designsystem.R.drawable.ic_main_sky),
        contentDescription = "IMG_MAIN_SKY",
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = 1.2f
                scaleY = 1.2f
            },
    )
}

@Composable
private fun HomeCharacterAnimation(
    modifier: Modifier = Modifier,
    fortuneScore: Int,
    hasActivatedAlarm: Boolean,
    eventDispatcher: (HomeContract.Action) -> Unit,
) {
    val (bubbleRes, starRes) = when {
        !hasActivatedAlarm -> {
            Pair(null, core.designsystem.R.raw.fortune_preload)
        }
        fortuneScore in 0..49 -> {
            Pair(
                core.designsystem.R.drawable.ic_fortune_0_to_49_speech_bubble,
                core.designsystem.R.raw.fortune_0_to_49,
            )
        }
        fortuneScore in 50..79 -> {
            Pair(
                core.designsystem.R.drawable.ic_fortune_50_to_79_speech_bubble,
                core.designsystem.R.raw.fortune_50_to_79,
            )
        }
        fortuneScore in 80..100 -> {
            Pair(
                core.designsystem.R.drawable.ic_fortune_80_to_100_speech_bubble,
                core.designsystem.R.raw.fortune_80_to_100,
            )
        }
        else -> {
            Pair(
                core.designsystem.R.drawable.ic_fortune_preload_speech_bubble,
                core.designsystem.R.raw.fortune_preload,
            )
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        bubbleRes?.let {
            Image(
                modifier = Modifier.height(46.dp),
                painter = painterResource(id = it),
                contentDescription = "IMG_MAIN_SPEECH_BUBBLE",
            )
            Spacer(modifier = Modifier.height(16.dp))
        } ?: Spacer(modifier = Modifier.height(62.dp))
        LottieAnimation(
            modifier = Modifier
                .size(110.dp)
                .clickable {
                    eventDispatcher(HomeContract.Action.FakeAction)
                },
            resId = starRes,
        )
    }
}

@Composable
private fun HomeFortuneDescription(
    modifier: Modifier = Modifier,
    fortuneScore: Int,
    hasActivatedAlarm: Boolean,
    name: String,
    deliveryTime: String,
) {
    val descriptionRes = when {
        !hasActivatedAlarm -> R.string.home_fortune_no_alarm_description
        fortuneScore in 0..49 -> R.string.home_fortune_0_to_49_description
        fortuneScore in 50..79 -> R.string.home_fortune_50_to_79_description
        fortuneScore in 80..100 -> R.string.home_fortune_80_to_100_description
        else -> R.string.home_fortune_preload_description
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = deliveryTime,
            style = OrbitTheme.typography.label1Medium,
            color = OrbitTheme.colors.white.copy(
                alpha = 0.7f,
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = descriptionRes, name),
            style = OrbitTheme.typography.heading2SemiBold,
            color = OrbitTheme.colors.white,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun HomeAlarmEmptyScreen(
    onSettingClick: () -> Unit,
    onMailClick: () -> Unit,
    onAddClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OrbitTheme.colors.gray_900),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HomeTopBar(
            onSettingClick = onSettingClick,
            onMailClick = onMailClick,
        )

        Spacer(modifier = Modifier.heightForScreenPercentage(0.13f))

        Image(
            painter = painterResource(
                id = core.designsystem.R.drawable.ic_no_alarm_speech_bubble,
            ),
            contentDescription = "IMG_MAIN_SPEECH_BUBBLE",
        )
        Image(
            painter = painterResource(
                id = core.designsystem.R.drawable.ic_main_no_alarm_star,
            ),
            contentDescription = "IMG_MAIN_STAR_GRAY",
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.home_empty_title),
            style = OrbitTheme.typography.heading1SemiBold,
            color = OrbitTheme.colors.white,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(id = R.string.home_empty_description),
            style = OrbitTheme.typography.body1Regular,
            color = OrbitTheme.colors.gray_300,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(32.dp))

        AddAlarmButton(
            onClick = onAddClick,
        )
    }
}

@Composable
private fun AddAlarmButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    height: Dp = 54.dp,
    containerColor: Color = OrbitTheme.colors.main,
    contentColor: Color = OrbitTheme.colors.gray_900,
    pressedContainerColor: Color = OrbitTheme.colors.main.copy(alpha = 0.8f),
    pressedContentColor: Color = OrbitTheme.colors.gray_600,
    disabledContainerColor: Color = OrbitTheme.colors.main.copy(alpha = 0.6f),
    disabledContentColor: Color = OrbitTheme.colors.gray_400,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val (currentContainerColor, currentContentColor) = when {
        !enabled -> disabledContainerColor to disabledContentColor
        isPressed -> pressedContainerColor to pressedContentColor
        else -> containerColor to contentColor
    }

    val padding by animateDpAsState(
        targetValue = if (isPressed) 2.dp else 0.dp,
        animationSpec = tween(durationMillis = 100),
        label = "PaddingAnimation",
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = currentContainerColor,
            contentColor = currentContentColor,
        ),
        contentPadding = PaddingValues(horizontal = 32.dp),
        interactionSource = interactionSource,
        modifier = modifier
            .then(if (padding > 0.dp) Modifier.padding(padding) else Modifier)
            .height(height - padding * 2),
    ) {
        Icon(
            painter = painterResource(core.designsystem.R.drawable.ic_plus),
            tint = Color.Unspecified,
            contentDescription = "Add Alarm",
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = stringResource(id = R.string.home_btn_add_alarm),
            style = OrbitTheme.typography.heading1SemiBold,
        )
    }
}

@Composable
private fun DeleteAlarmButton(
    modifier: Modifier = Modifier,
    selectedAlarmCount: Int,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val padding by animateDpAsState(
        targetValue = if (isPressed) 2.dp else 0.dp,
        animationSpec = tween(durationMillis = 100),
        label = "PaddingAnimation",
    )

    val (containerColor, contentColor) = if (isPressed) {
        OrbitTheme.colors.white.copy(alpha = 0.8f) to OrbitTheme.colors.gray_600
    } else {
        OrbitTheme.colors.white to OrbitTheme.colors.gray_900
    }

    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        contentPadding = PaddingValues(horizontal = 40.dp),
        modifier = modifier
            .then(if (padding > 0.dp) Modifier.padding(padding) else Modifier)
            .height(54.dp - padding * 2),
    ) {
        Text(
            text = stringResource(
                id = R.string.alarm_list_bottom_sheet_selection_btn_delete,
                selectedAlarmCount,
            ),
            style = OrbitTheme.typography.body1SemiBold,
        )
    }
}

@Composable
private fun BottomGradient(
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF17171B).copy(alpha = 0f),
                        Color(0xFF17171B).copy(alpha = 1f),
                    ),
                ),
            ),
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
)
@Composable
fun HomeScreenPreview() {
    OrbitTheme {
        HomeScreen(
            stateProvider = { HomeContract.State() },
            eventDispatcher = {},
        )
    }
}
