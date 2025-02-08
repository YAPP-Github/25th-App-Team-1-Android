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
import com.yapp.ui.utils.paddingForScreenPercentage
import core.designsystem.R

@Composable
fun FortuneFirstPage() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_100_buble),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .paddingForScreenPercentage(topPercentage = 0.04f),
        )
        FortuneCharacter(
            modifier = Modifier
                .paddingForScreenPercentage(topPercentage = 0.12f)
                .zIndex(1f)
                .align(Alignment.TopCenter),
            fortuneScore = 100,
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
                    text = "오늘은 괜찮은 하루가 될 거야! 평소보다 긍정적인 마음으로 하루를 시작하면 좋은 일이 생길지도 몰라. 주변 사람들과의 관계에 신경 쓰면 더욱 행복한 하루가 될 거야. 혹시 오늘 중요한 일이 있다면, 미리 계획을 세우고 차분하게 진행하는 게 좋아. 너의 꼼꼼함이 빛을 발할 거야!",
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
    FortuneFirstPage()
}
