package com.yapp.home

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.domain.model.Alarm
import com.yapp.ui.utils.heightForScreenPercentage
import feature.home.R

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
) {
    HomeScreen()
}

@Composable
fun HomeScreen() {
    val systemUiController = rememberSystemUiController()

    val statusBarColor = OrbitTheme.colors.gray_900

    DisposableEffect(Unit) {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
        )
        Log.d("HomeScreen", "statusBarColor: Transparent")
        onDispose {
            systemUiController.setStatusBarColor(
                color = statusBarColor,
            )
        }
    }

    val alarms = listOf(
        Alarm(
            id = 1,
            hour = 7,
            minute = 30,
        ),
        Alarm(
            id = 2,
            hour = 6,
            minute = 30,
        ),
        Alarm(
            id = 3,
            hour = 8,
            minute = 30,
        ),
        Alarm(
            id = 4,
            hour = 10,
            minute = 30,
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OrbitTheme.colors.gray_900),
    ) {
        HomeTopBar(
            onSettingClick = {
            },
            onMailClick = {
            },
        )

        Spacer(modifier = Modifier.heightForScreenPercentage(0.13f))

        HomeEmpty()
    }
}

@Composable
private fun HomeTopBar(
    onSettingClick: () -> Unit,
    onMailClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 12.dp,
            ),
    ) {
        Text(
            text = stringResource(id = R.string.home_top_bar_title),
            style = OrbitTheme.typography.heading1SemiBold.copy(
                fontWeight = FontWeight.Bold,
            ),
            color = OrbitTheme.colors.main,
        )

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

@Composable
private fun HomeEmpty() {
    val colorMatrix = ColorMatrix().apply {
        setToSaturation(0f)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(
                id = core.designsystem.R.mipmap.img_main_star,
            ),
            contentDescription = "IMG_MAIN_STAR_GRAY",
            colorFilter = ColorFilter.colorMatrix(colorMatrix),
        )

        Spacer(modifier = Modifier.height(35.dp))

        Text(
            text = stringResource(id = R.string.home_empty_title),
            style = OrbitTheme.typography.heading1SemiBold,
            color = OrbitTheme.colors.white,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.home_empty_description),
            style = OrbitTheme.typography.body1Regular,
            color = OrbitTheme.colors.gray_300,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(32.dp))

        AddAlarmButton(
            modifier = Modifier.padding(horizontal = 86.dp),
            onClick = {
            },
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
            .fillMaxWidth()
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
        contentPadding = PaddingValues(0.dp),
        interactionSource = interactionSource,
    ) {
        Icon(
            painter = painterResource(core.designsystem.R.drawable.ic_plus),
            tint = Color.Unspecified,
            contentDescription = "IC_PLUS",
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = "새 알람 추가하기",
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
    HomeScreen()
}
