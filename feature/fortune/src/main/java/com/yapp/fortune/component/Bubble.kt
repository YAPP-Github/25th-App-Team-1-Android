package com.yapp.fortune.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme

@Composable
fun Bubble(
    modifier: Modifier = Modifier,
    text: String,
) {
    Box(
        modifier = modifier
            .background(
                color = OrbitTheme.colors.white.copy(alpha = 0.2f),
                shape = RoundedCornerShape(20.dp),
            )
            .padding(vertical = 10.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = OrbitTheme.colors.white,
            style = OrbitTheme.typography.body1Medium,
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
