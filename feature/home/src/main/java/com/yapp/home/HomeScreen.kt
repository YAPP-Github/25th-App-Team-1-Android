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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.yapp.ui.component.lottie.LottieAnimation
import com.yapp.ui.lifecycle.LaunchedEffectWithLifecycle
import com.yapp.ui.utils.heightForScreenPercentage
import com.yapp.ui.utils.toPx
import feature.home.R

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    navigator: OrbitNavigator,
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()
    val sideEffect = viewModel.container.sideEffectFlow

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

    if (state.alarms.isEmpty()) {
        HomeAlarmEmptyScreen(
            onSettingClick = { },
            onMailClick = { },
            onAddClick = { },
        )
    } else {
        HomeContent(state)
    }
}

@Composable
private fun HomeContent(state: HomeContract.State) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1F3B64)),
    ) {
        HillWithGradient()

        SkyImage()

        val characterY = (LocalConfiguration.current.screenHeightDp.dp * 0.28f) - 130.dp

        HomeCharacterAnimation(
            modifier = Modifier
                .offset(y = characterY)
                .align(Alignment.TopCenter),
            fortuneScore = state.lastFortuneScore,
        )

        HomeTopBar(
            isTitleVisible = false,
            onSettingClick = { },
            onMailClick = { },
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
) {
    val (bubbleRes, starRes) = when (fortuneScore) {
        in 0..49 -> {
            Pair(
                core.designsystem.R.drawable.ic_fortune_0_to_49_speech_bubble,
                core.designsystem.R.raw.fortune_0_to_49,
            )
        }
        in 50..79 -> {
            Pair(
                core.designsystem.R.drawable.ic_fortune_50_to_79_speech_bubble,
                core.designsystem.R.raw.fortune_50_to_79,
            )
        }
        in 80..100 -> {
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
        Image(
            painter = painterResource(id = bubbleRes),
            contentDescription = "IMG_MAIN_SPEECH_BUBBLE",
        )
        Spacer(modifier = Modifier.height(16.dp))
        LottieAnimation(
            modifier = Modifier.size(110.dp),
            resId = starRes,
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
    val isPressed = interactionSource.collectIsPressedAsState().value

    val currentContainerColor = if (isPressed) pressedContainerColor else containerColor
    val currentContentColor = if (isPressed) pressedContentColor else contentColor

    val padding by animateDpAsState(
        targetValue = if (isPressed) 2.dp else 0.dp,
        animationSpec = tween(durationMillis = 100),
        label = "",
    )

    Button(
        onClick = onClick,
        modifier = modifier
            .padding(all = padding)
            .height(height),
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = currentContainerColor,
            contentColor = currentContentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
        ),
        contentPadding = PaddingValues(
            horizontal = 32.dp,
        ),
        interactionSource = interactionSource,
    ) {
        Icon(
            painter = painterResource(core.designsystem.R.drawable.ic_plus),
            tint = Color.Unspecified,
            contentDescription = "IC_PLUS",
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = stringResource(id = R.string.home_btn_add_alarm),
            style = OrbitTheme.typography.heading1SemiBold,
        )
    }
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
