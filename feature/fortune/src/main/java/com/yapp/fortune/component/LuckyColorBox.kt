package com.yapp.fortune.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme

@Composable
fun LuckyColorBox(
    colorTitle: String,
    resId: Int,
    contentLabel: String,
    colorTint: Color,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = colorTitle,
            style = OrbitTheme.typography.heading2SemiBold,
            color = OrbitTheme.colors.gray_900,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.wrapContentSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = resId),
                contentDescription = null,
                tint = colorTint,
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = contentLabel,
                style = OrbitTheme.typography.body1Regular,
                color = OrbitTheme.colors.gray_600,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LuckyColorBoxPreview() {
    LuckyColorBox(
        colorTitle = "행운의 색",
        resId = core.designsystem.R.drawable.ic_circle,
        contentLabel = "회색",
        colorTint = Color.Gray,
    )
}
