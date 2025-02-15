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
    fortuneSubTitleText: String,
    fortuneContentText: String,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .paddingForScreenPercentage(horizontalPercentage = 0.055f),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                style = OrbitTheme.typography.label1SemiBold,
                color = color,
                textAlign = TextAlign.Start,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = fortuneSubTitleText,
            style = OrbitTheme.typography.body1Bold,
            color = OrbitTheme.colors.gray_600,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(5.dp))
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
@Preview
fun DetailHoroScopeTextPreview() {
    DetailHoroScopeText(
        color = Color.Red,
        iconRes = 0,
        fortuneTitleText = "학업",
        fortuneSubTitleText = "오늘의 운세",
        fortuneContentText = "오늘은 학업에 대한 운세가 좋습니다.",
    )
}
