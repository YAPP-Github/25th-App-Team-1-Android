package com.kms.onboarding.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.utils.paddingForScreenPercentage

@Composable
fun OnboardingGenderToggle(
    label: String,
    isSelected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor = when {
        isPressed -> OrbitTheme.colors.gray_700
        isSelected -> OrbitTheme.colors.main.copy(alpha = 0.1f)
        else -> OrbitTheme.colors.gray_800
    }
    val borderColor = when {
        isSelected -> OrbitTheme.colors.main.copy(alpha = 0.4f)
        isPressed -> Color.Transparent
        else -> OrbitTheme.colors.gray_600
    }
    val contentColor = when {
        isPressed -> OrbitTheme.colors.white
        isSelected -> OrbitTheme.colors.sub_main
        else -> OrbitTheme.colors.white
    }

    val innerSizeScale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "SizeAnimation",
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .wrapContentSize()
            .border(
                width = if (isPressed) 0.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp),
            )
            .background(backgroundColor, shape = RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = !isSelected,
            ) {
                onToggle()
            },
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .paddingForScreenPercentage(verticalPercentage = 0.074f, horizontalPercentage = 0.144f)
                .wrapContentSize()
                .scale(innerSizeScale),
        ) {
            Text(
                text = label,
                color = contentColor,
                style = OrbitTheme.typography.heading2SemiBold,
            )
        }
    }
}

@Preview
@Composable
fun PreviewSquareToggleButton() {
    var isSelected by remember { mutableStateOf(false) }

    OnboardingGenderToggle(
        label = "남성",
        isSelected = isSelected,
        onToggle = { isSelected = !isSelected },
    )
}
