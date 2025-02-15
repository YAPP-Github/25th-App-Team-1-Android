package com.yapp.fortune.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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
fun FortuneFirstPage(
    dailyFortuneTitle: String,
    dailyFortuneDescription: String,
    avgFortuneScore: Int,
) {
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
                modifier = Modifier.align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_letter),
                    contentDescription = null,
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(horizontal = 40.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(top = 30.dp)
                            .align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        item {
                            Text(
                                text = dailyFortuneTitle,
                                style = OrbitTheme.typography.H2,
                                color = OrbitTheme.colors.gray_600,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                maxLines = Int.MAX_VALUE,
                                softWrap = true,
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        item {
                            Text(
                                text = dailyFortuneDescription,
                                style = OrbitTheme.typography.H4_150,
                                color = OrbitTheme.colors.gray_600,
                                modifier = Modifier.width(IntrinsicSize.Max),
                                maxLines = Int.MAX_VALUE,
                                softWrap = true,
                                textAlign = TextAlign.Center,
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        item {
                            Text(
                                text = "From. 오르비",
                                style = OrbitTheme.typography.H5,
                                color = OrbitTheme.colors.gray_600,
                                modifier = Modifier.padding(bottom = 20.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun FortuneFirstPagePreview() {
    FortuneFirstPage(
        dailyFortuneTitle = "",
        avgFortuneScore = 0,
        dailyFortuneDescription = "",
    )
}
