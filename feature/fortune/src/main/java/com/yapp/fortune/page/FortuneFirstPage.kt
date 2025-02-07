package com.yapp.fortune.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
            Box {
                Image(
                    painter = painterResource(id = R.drawable.ic_letter),
                    contentDescription = null,
                )

                Text(
                    text = "오늘은 네가 가진 성실함과 진정성이\n빛을 발할 수 있는 날이야. 중요한 결정\n은 신중히, 감정적 말은 삼가고 차분히 대\n처하면 좋아. 주변 사람들과 긍정적으로 소\n통하고, 계획을 재정비하는 데 시간 써봐.\n 파란색이나 녹색 아이템이 너의 운을 더\n 북돋아줄거야! 오늘도 화이팅 하자! 오늘도\n 화이팅 하자! 오늘도 화이팅 하자! 오늘도 ",
                    style = OrbitTheme.typography.H3,
                    color = OrbitTheme.colors.gray_600,
                    modifier = Modifier
                        .align(Alignment.Center),
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
