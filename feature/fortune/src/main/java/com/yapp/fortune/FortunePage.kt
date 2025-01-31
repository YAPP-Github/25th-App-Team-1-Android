package com.yapp.fortune

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.fortune.component.Bubble
import com.yapp.ui.utils.heightForScreenPercentage
import com.yapp.ui.utils.paddingForScreenPercentage

@Composable
fun FortunePage(page: Int) {
    when (page) {
        0 -> FortuneFirstPage()
        1 -> FortuneSecondPage()
        2 -> FortuneThirdPage()
        else -> DefaultFortunePage(page)
    }
}

@Composable
fun FortuneFirstPage() {
    val letterY = (LocalConfiguration.current.screenHeightDp.dp * 0.12f)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = core.designsystem.R.drawable.ic_letter),
            contentDescription = null,
            modifier = Modifier.offset(y = letterY),
        )
    }
}

@Composable
fun FortuneSecondPage() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Bubble(
                modifier = Modifier.paddingForScreenPercentage(topPercentage = 0.056f),
                text = "오늘의 운세",
            )
            Text(
                text = "오늘 강문수의 하루는 \n" + "행운이 가득해!",
                style = OrbitTheme.typography.H2,
                color = OrbitTheme.colors.white,
                modifier = Modifier.padding(top = 20.dp),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.heightForScreenPercentage(0.046f))
            Image(
                painter = painterResource(id = core.designsystem.R.drawable.ic_letter_horoscope),
                contentDescription = null,
                modifier = Modifier.padding(top = 20.dp),
            )
        }
    }
}

@Composable
fun FortuneThirdPage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {}
}

@Composable
fun DefaultFortunePage(page: Int) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {}
}

@Composable
@Preview()
fun FortunePagePreview() {
    FortunePage(1)
}
