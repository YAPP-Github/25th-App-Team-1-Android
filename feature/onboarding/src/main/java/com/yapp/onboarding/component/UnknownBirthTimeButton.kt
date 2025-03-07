package com.yapp.onboarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import feature.onboarding.R

@Composable
fun UnknownBirthTimeButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    containerColor: Color = Color.Transparent,
    contentColor: Color = OrbitTheme.colors.white,
    pressedContainerColor: Color = OrbitTheme.colors.gray_800,
    pressedContentColor: Color = OrbitTheme.colors.main,
    shape: Shape = RoundedCornerShape(12.dp),
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentContainerColor = if (isPressed) pressedContainerColor else containerColor
    val currentContentColor = if (isPressed) pressedContentColor else contentColor

    Row(
        modifier = modifier
            .background(currentContainerColor, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 24.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(id = R.string.onboarding_step4_text_check),
            style = OrbitTheme.typography.body1Medium,
            color = currentContentColor,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            painter = painterResource(id = core.designsystem.R.drawable.ic_arrow_right),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = currentContentColor,
        )
    }
}

@Composable
@Preview
fun PreviewUnknownBirthTimeButton() {
    UnknownBirthTimeButton(
        onClick = { /*TODO*/ },
    )
}
