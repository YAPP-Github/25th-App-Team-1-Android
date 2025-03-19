package com.yapp.fortune.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.yapp.analytics.AnalyticsEvent
import com.yapp.analytics.LocalAnalyticsHelper
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.fortune.component.HillWithGradient
import com.yapp.ui.component.button.OrbitButton
import com.yapp.ui.component.lottie.LottieAnimation
import com.yapp.ui.utils.paddingForScreenPercentage

/**
 * 운세 확인 완료 페이지
 */
@Composable
fun FortuneCompletePage(
    hasReward: Boolean,
    onCompleteClick: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    val analyticsHelper = LocalAnalyticsHelper.current

    val message = if (hasReward) {
        "첫 알람에 잘 일어났네!\n보상으로 행운 부적을 줄게"
    } else {
        "운세 확인 끝!\n이제 든든하게 하루 시작해봐"
    }

    val animationResId = if (hasReward) {
        core.designsystem.R.raw.reward_true
    } else {
        core.designsystem.R.raw.reward_false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HillWithGradient(heightPercentage = 0.48f)

        LottieAnimation(
            modifier = Modifier
                .scale(0.5f)
                .zIndex(1f)
                .paddingForScreenPercentage(topPercentage = if (hasReward) 0.12f else 0.0f),
            resId = animationResId,
        )

        Text(
            text = message,
            style = OrbitTheme.typography.H1,
            color = OrbitTheme.colors.white,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 46.dp)
                .align(Alignment.TopCenter),
            textAlign = TextAlign.Center,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "오늘의 운세는 하루 동안 홈 화면에서 볼 수 있어요.",
                style = OrbitTheme.typography.body2Regular,
                color = OrbitTheme.colors.white.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            OrbitButton(
                label = if (hasReward) "부적 보러가기" else "완료",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                onClick = {
                    analyticsHelper.logEvent(
                        AnalyticsEvent(
                            type = "fortune_complete",
                        ),
                    )
                    if (hasReward) {
                        onCompleteClick()
                    } else {
                        onNavigateToHome()
                    }
                },
                enabled = true,
            )
        }
    }
}

@Composable
@Preview
fun FortuneCompletePagePreview() {
    FortuneCompletePage(hasReward = true, onCompleteClick = {}, onNavigateToHome = {})
}
