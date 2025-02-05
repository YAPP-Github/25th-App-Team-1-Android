package com.yapp.fortune.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.zIndex
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
                .paddingForScreenPercentage(topPercentage = 0.34f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_letter),
                contentDescription = null,
            )
        }
    }
}
