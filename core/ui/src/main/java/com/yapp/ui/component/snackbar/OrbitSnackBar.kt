package com.yapp.ui.component.snackbar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme

@Composable
fun OrbitSnackBar(
    label: String,
    message: String,
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int? = null,
    onAction: () -> Unit,
) {
    Snackbar(
        modifier = modifier.clip(RoundedCornerShape(12.dp)),
        action = {
            Text(
                modifier = Modifier
                    .clickable(
                        onClick = onAction,
                    )
                    .padding(end = 4.dp),
                color = OrbitTheme.colors.gray_50,
                style = OrbitTheme.typography.label2Regular,
                text = label,
            )
        },
        containerColor = OrbitTheme.colors.gray_500,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // 아이콘 표시
            iconRes?.let {
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp), // 아이콘과 메시지 간 간격 추가
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(id = it),
                        contentDescription = "Check Icon",
                    )
                }
            }

            Text(
                color = OrbitTheme.colors.white,
                style = OrbitTheme.typography.label1Medium,
                text = message,
            )
        }
    }
}

@Composable
@Preview
fun OrbitSnackBarPreview() {
    OrbitTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            OrbitSnackBar(
                modifier = Modifier
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 12.dp,
                    )
                    .align(Alignment.BottomCenter),
                label = "label",
                message = "Hello, World!",
                iconRes = core.designsystem.R.drawable.ic_check_green,
                onAction = {},
            )
        }
    }
}
