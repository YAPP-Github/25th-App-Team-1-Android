package com.yapp.fortune.page.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.fortune.component.LuckyColorBox

/**
 * 행운의 색상 정보
 */
@Composable
fun LuckyColorContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LuckyColorBox(
            colorTitle = "행운의 색",
            resId = core.designsystem.R.drawable.ic_circle,
            contentLabel = "색상1",
            colorTint = OrbitTheme.colors.green,
        )
        Spacer(modifier = Modifier.height(52.dp))
        LuckyColorBox(
            colorTitle = "피해야할 색",
            resId = core.designsystem.R.drawable.ic_circle,
            contentLabel = "색상2",
            colorTint = OrbitTheme.colors.red,
        )
        Spacer(modifier = Modifier.height(52.dp))
        LuckyColorBox(
            colorTitle = "추천 음식",
            resId = core.designsystem.R.drawable.ic_food,
            contentLabel = "햄버거",
            colorTint = Color.Unspecified,
        )
    }
}
