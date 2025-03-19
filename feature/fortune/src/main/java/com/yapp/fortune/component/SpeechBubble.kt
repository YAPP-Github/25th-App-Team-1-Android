package com.yapp.fortune.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme

@Composable
fun SpeechBubble(
    modifier: Modifier = Modifier,
    text: String,
    backGroundColor: Color = OrbitTheme.colors.white.copy(alpha = 0.2f),
    textColor: Color = OrbitTheme.colors.white,
    textStyle: TextStyle = OrbitTheme.typography.H3,
    verticalPadding: Dp = 10.dp,
    horizontalPadding: Dp = 16.dp,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = backGroundColor,
                    shape = RoundedCornerShape(20.dp),
                )
                .padding(vertical = verticalPadding, horizontal = horizontalPadding),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                color = textColor,
                style = textStyle,
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        Icon(
            painter = painterResource(id = core.designsystem.R.drawable.ic_inverted_triangle),
            contentDescription = null,
            tint = Color.Unspecified,
        )
    }
}

@Composable
@Preview
fun SpeechBubblePreview() {
    SpeechBubble(
        modifier = Modifier,
        text = "오늘의 운세",
    )
}
