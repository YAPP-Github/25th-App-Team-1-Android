package com.yapp.setting.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme

@Composable
fun InquiryCard(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = OrbitTheme.colors.gray_900,
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 13.dp),
        ) {
            InquiryCardTitle(title = "오르비는 여러분과 함께\n" + "성장해요!")
            Spacer(modifier = Modifier.height(12.dp))
            InquirySendRow(sendTitle = "의견 보내기")
        }
        Image(
            painter = painterResource(id = core.designsystem.R.drawable.ic_orbit_write_fortune),
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterEnd),
            contentDescription = "",
        )
    }
}

@Composable
fun InquiryCardTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier
            .wrapContentWidth()
            .padding(start = 4.dp),
        style = OrbitTheme.typography.headline2SemiBold,
        color = OrbitTheme.colors.gray_100,
    )
}

@Composable
fun InquirySendRow(sendTitle: String) {
    Row(
        modifier = Modifier
            .wrapContentWidth()
            .clickable(onClick = { /* TODO */ })
            .padding(horizontal = 4.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = sendTitle,
            modifier = Modifier.wrapContentWidth(),
            style = OrbitTheme.typography.label1Medium,
            color = OrbitTheme.colors.main,
        )
        Icon(
            painter = painterResource(id = core.designsystem.R.drawable.ic_arrow_right),
            contentDescription = "",
            modifier = Modifier.size(20.dp),
            tint = OrbitTheme.colors.main,
        )
    }
}

@Composable
@Preview
fun InquiryCardPreview() {
    InquiryCard()
}
