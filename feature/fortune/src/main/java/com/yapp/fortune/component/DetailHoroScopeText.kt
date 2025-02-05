package com.yapp.fortune.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.utils.paddingForScreenPercentage

@Composable
fun DetailHoroScopeText(
    modifier: Modifier = Modifier,
    color: Color,
    @DrawableRes iconRes: Int,
    fortuneTitleText: String,
    fortuneContentText: String,
) {
    Column(
        modifier = modifier
            .fillMaxWidth() // 🔥 너비 통일
            .paddingForScreenPercentage(horizontalPercentage = 0.170f),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), // 🔥 너비 맞추기
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 4.dp),
            )
            Text(
                text = fortuneTitleText,
                style = OrbitTheme.typography.headline1SemiBold,
                color = color,
                textAlign = TextAlign.Start,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = fortuneContentText,
            style = OrbitTheme.typography.body2Regular,
            color = OrbitTheme.colors.gray_600,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun DetailHoroScopeTextPreview() {
    DetailHoroScopeText(
        modifier = Modifier,
        fortuneTitleText = "학업/직장운 88점",
        color = OrbitTheme.colors.gray_600,
        iconRes = core.designsystem.R.drawable.ic__graduationcap,
        fortuneContentText = "오늘은 학업이나 직장에서 좋은 결과를 얻을 수 있는 날입니다. 무엇이든 도전해보세요!",
    )
}
