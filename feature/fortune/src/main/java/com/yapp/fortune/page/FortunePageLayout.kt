package com.yapp.fortune.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.fortune.component.Bubble
import com.yapp.fortune.page.content.CodyContent
import com.yapp.fortune.page.content.HoroscopeContent
import com.yapp.fortune.page.content.LuckyColorContent
import com.yapp.ui.utils.heightForScreenPercentage
import com.yapp.ui.utils.paddingForScreenPercentage

/**
 * 운세 페이지 공통 레이아웃(코디, 행운의 색상, 오늘의 운세)
 */
@Composable
fun FortunePageLayout(data: FortunePageData) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Bubble(
                modifier = Modifier.paddingForScreenPercentage(topPercentage = 0.03f),
                text = data.title,
            )
            Text(
                text = data.description,
                style = OrbitTheme.typography.H2,
                color = OrbitTheme.colors.white,
                modifier = Modifier.padding(top = 20.dp),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.heightForScreenPercentage(0.046f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .paddingForScreenPercentage(horizontalPercentage = 0.1f),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = data.backgroundResId),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                )

                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    when {
                        data.isHoroscopePage -> {
                            if (data.details.isEmpty()) {
                                Text(
                                    text = "운세 데이터를 불러올 수 없습니다.",
                                    style = OrbitTheme.typography.H2,
                                    color = Color.Red,
                                )
                            } else {
                                HoroscopeContent(details = data.details) // ✅ 운세 점수 및 설명
                            }
                        }

                        data.isCodyPage -> CodyContent(
                            luckyOutfitTop = data.luckyOutfitTop,
                            luckyOutfitBottom = data.luckyOutfitBottom,
                            luckyOutfitShoes = data.luckyOutfitShoes,
                            luckyOutfitAccessory = data.luckyOutfitAccessory,
                        )

                        data.isLuckyColorPage -> LuckyColorContent(
                            luckyColor = data.luckyColor,
                            unluckyColor = data.unluckyColor,
                            luckyFood = data.luckyFood,
                        )
                    }
                }
            }
        }
    }
}
