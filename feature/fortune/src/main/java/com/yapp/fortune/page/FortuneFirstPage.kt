package com.yapp.fortune.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.fortune.component.FortuneCharacter
import com.yapp.fortune.component.HillWithGradient
import com.yapp.fortune.component.SpeechBubble
import com.yapp.ui.utils.paddingForScreenPercentage
import core.designsystem.R

@Composable
fun FortuneFirstPage(dailyFortune: String, avgFortuneScore: Int) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        SpeechBubble(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .paddingForScreenPercentage(topPercentage = 0.055f),
            text = "오늘의 운세 점수 ${avgFortuneScore}점",
        )
        FortuneCharacter(
            modifier = Modifier
                .paddingForScreenPercentage(topPercentage = 0.12f)
                .zIndex(1f)
                .align(Alignment.TopCenter),
            fortuneScore = avgFortuneScore,
        )

        HillWithGradient(
            heightPercentage = 0.24f,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .paddingForScreenPercentage(topPercentage = 0.30f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.wrapContentSize(),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_letter),
                    contentDescription = null,
                )
                Text(
                    text = dailyFortune,
                    style = OrbitTheme.typography.H3,
                    color = OrbitTheme.colors.gray_600,
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .align(Alignment.Center)
                        .padding(horizontal = 60.dp),
                    maxLines = Int.MAX_VALUE,
                    softWrap = true,
                )
                Text(
                    text = "From. 오르비",
                    style = OrbitTheme.typography.H4,
                    color = OrbitTheme.colors.gray_600,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 20.dp),
                )
            }
        }
    }
}

@Composable
@Preview
fun FortuneFirstPagePreview() {
    FortuneFirstPage(
        dailyFortune = "",
        avgFortuneScore = 0,
    )
}
