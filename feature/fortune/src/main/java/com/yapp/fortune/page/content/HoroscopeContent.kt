package com.yapp.fortune.page.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.domain.model.fortune.Fortune
import com.yapp.fortune.component.DetailHoroScopeText
import core.designsystem.R

/**
 * Fortune 상세 정보 리스트 (오늘의 운세)
 */
@Composable
fun HoroscopeContent(details: List<Fortune>) {
    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        details.forEachIndexed { index, detail ->
            DetailHoroScopeText(
                fortuneTitleText = detail.contentTitle,
                fortuneContentText = detail.contentDescription,
                color = getFortuneColor(detail.contentTitle, index),
                iconRes = getFortuneIcon(detail.contentTitle, index),
            )
            if (index < details.size - 1) Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun getFortuneColor(contentTitle: String, index: Int): Color {
    return when {
        contentTitle.contains("학업") -> OrbitTheme.colors.blue_2
        contentTitle.contains("연애") -> OrbitTheme.colors.babypink
        contentTitle.contains("건강") -> OrbitTheme.colors.green
        contentTitle.contains("애정") -> OrbitTheme.colors.pink
        else -> listOf(
            OrbitTheme.colors.blue_2,
            OrbitTheme.colors.babypink,
            OrbitTheme.colors.green,
            OrbitTheme.colors.pink,
        ).getOrElse(index) { OrbitTheme.colors.gray }
    }
}

fun getFortuneIcon(contentTitle: String, index: Int): Int {
    return when {
        contentTitle.contains("학업") -> core.designsystem.R.drawable.ic__graduationcap
        contentTitle.contains("연애") -> core.designsystem.R.drawable.ic_piggybank
        contentTitle.contains("건강") -> core.designsystem.R.drawable.ic_health
        contentTitle.contains("애정") -> core.designsystem.R.drawable.ic__affection
        else -> listOf(
            core.designsystem.R.drawable.ic__graduationcap,
            core.designsystem.R.drawable.ic_piggybank,
            core.designsystem.R.drawable.ic_health,
            core.designsystem.R.drawable.ic__affection,
        ).getOrElse(index) { R.drawable.ic_circle }
    }
}
