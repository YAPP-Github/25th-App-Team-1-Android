package com.yapp.fortune.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme

@Composable
fun Bubble(
    modifier: Modifier = Modifier,
    text: String,
    backGroundColor: Color = OrbitTheme.colors.white.copy(alpha = 0.2f),
    textColor: Color = OrbitTheme.colors.white,
    textStyle: TextStyle = OrbitTheme.typography.body1Medium,
) {
    Box(
        modifier = modifier
            .background(
                color = backGroundColor,
                shape = RoundedCornerShape(20.dp),
            )
            .padding(vertical = 10.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = textColor,
            style = textStyle,
        )
    }
}

@Composable
@Preview
fun BubblePreview() {
    Bubble(
        modifier = Modifier,
        text = "오늘의 운세",
    )
}
