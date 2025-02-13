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
fun LuckyColorContent(
    luckyColor: String,
    unluckyColor: String,
    luckyFood: String,
) {
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
            contentLabel = luckyColor,
            colorTint = getColorFromName(luckyColor),
        )
        Spacer(modifier = Modifier.height(52.dp))
        LuckyColorBox(
            colorTitle = "피해야할 색",
            resId = core.designsystem.R.drawable.ic_circle,
            contentLabel = unluckyColor,
            colorTint = getColorFromName(unluckyColor),
        )
        Spacer(modifier = Modifier.height(52.dp))
        LuckyColorBox(
            colorTitle = "추천 음식",
            resId = core.designsystem.R.drawable.ic_food,
            contentLabel = luckyFood,
            colorTint = Color.Unspecified,
        )
    }
}

@Composable
fun getColorFromName(colorName: String): Color {
    return when (colorName.lowercase()) {
        "빨강", "레드" -> OrbitTheme.colors.red
        "분홍", "핑크" -> OrbitTheme.colors.pink
        "주황", "오렌지" -> OrbitTheme.colors.orange
        "노랑", "옐로우" -> OrbitTheme.colors.yellow
        "초록", "그린" -> OrbitTheme.colors.green
        "파랑", "블루" -> OrbitTheme.colors.blue
        "보라", "퍼플" -> OrbitTheme.colors.purple
        "갈색", "브라운" -> OrbitTheme.colors.brown
        "회색", "그레이" -> OrbitTheme.colors.gray
        "인디고" -> OrbitTheme.colors.indigo
        else -> OrbitTheme.colors.gray_600
    }
}
