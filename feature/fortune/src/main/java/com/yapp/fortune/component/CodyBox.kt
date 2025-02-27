package com.yapp.fortune.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
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

@Composable
fun CodyBox(
    resId: Int,
    categoryLabel: String,
    clothsNameLabel: String,
) {
    Column(
        modifier = Modifier.width(110.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(id = resId),
            tint = Color.Unspecified,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Bubble(
            text = categoryLabel,
            backGroundColor = OrbitTheme.colors.gray_50,
            textColor = OrbitTheme.colors.gray_500,
            textStyle = OrbitTheme.typography.label2SemiBold,
            verticalPadding = 4.dp,
            horizontalPadding = 10.dp,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = clothsNameLabel,
            style = OrbitTheme.typography.body1Regular,
            color = OrbitTheme.colors.gray_600,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
@Preview
fun CodyBoxPreview() {
    CodyBox(
        resId = core.designsystem.R.drawable.ic_top,
        categoryLabel = "상의",
        clothsNameLabel = "흰색 반팔티",
    )
}
