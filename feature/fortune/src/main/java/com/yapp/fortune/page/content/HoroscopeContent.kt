package com.yapp.fortune.page.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.fortune.component.DetailHoroScopeText
import com.yapp.fortune.page.Contents
import core.designsystem.R

/**
 * Fortune 상세 정보 리스트 (오늘의 운세)
 */
@Composable
fun HoroscopeContent(details: List<Contents>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        items(details.chunked(2)) { columnItems ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                columnItems.forEach { detail ->
                    DetailHoroScopeText(
                        fortuneTitleText = detail.contentScore,
                        fortuneSubTitleText = detail.contentTitle,
                        fortuneContentText = detail.contentDescription,
                        color = getFortuneColor(detail.contentScore, details.indexOf(detail)),
                        iconRes = getFortuneIcon(detail.contentScore, details.indexOf(detail)),
                    )
                }
            }
        }
    }
}

@Composable
fun getFortuneColor(contentTitle: String, index: Int): Color {
    return when {
        contentTitle.contains("학업") -> OrbitTheme.colors.blue_2
        contentTitle.contains("애정") -> OrbitTheme.colors.pink
        contentTitle.contains("건강") -> OrbitTheme.colors.green
        contentTitle.contains("재물") -> OrbitTheme.colors.babypink

        else -> listOf(
            OrbitTheme.colors.blue_2,
            OrbitTheme.colors.pink,
            OrbitTheme.colors.green,
            OrbitTheme.colors.babypink,
        ).getOrElse(index) { OrbitTheme.colors.gray }
    }
}

fun getFortuneIcon(contentTitle: String, index: Int): Int {
    return when {
        contentTitle.contains("학업") -> core.designsystem.R.drawable.ic__graduationcap
        contentTitle.contains("애정") -> core.designsystem.R.drawable.ic__affection
        contentTitle.contains("건강") -> core.designsystem.R.drawable.ic_health
        contentTitle.contains("재물") -> core.designsystem.R.drawable.ic_piggybank

        else -> listOf(
            core.designsystem.R.drawable.ic__graduationcap,
            core.designsystem.R.drawable.ic__affection,
            core.designsystem.R.drawable.ic_health,
            core.designsystem.R.drawable.ic_piggybank,
        ).getOrElse(index) { R.drawable.ic_circle }
    }
}
