package com.yapp.ui.component.snackbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme

@Composable
fun OrbitSnackBar(
    label: String,
    message: String,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    onDismiss: () -> Unit,
) {
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        snackBarHostState.showSnackbar(
            message = message,
            actionLabel = label,
        )
    }

    SnackbarHost(
        hostState = snackBarHostState,
        modifier = modifier,
    ) { data ->
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                TextButton(
                    onClick = {
                        data.dismiss()
                        onDismiss()
                    },
                ) {
                    Text(
                        color = OrbitTheme.colors.white,
                        style = OrbitTheme.typography.body1SemiBold,
                        text = label,
                    )
                }
            },
            containerColor = OrbitTheme.colors.gray_500.copy(),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // 아이콘 표시
                icon?.let {
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp), // 아이콘과 메시지 간 간격 추가
                        contentAlignment = Alignment.Center,
                    ) {
                        it()
                    }
                }

                Text(
                    color = OrbitTheme.colors.white,
                    style = OrbitTheme.typography.body1SemiBold,
                    text = message,
                )
            }
        }
    }
}

@Composable
@Preview
fun OrbitSnackBarPreview() {
    OrbitSnackBar(
        label = "label",
        message = "Hello, World!",
        icon = {
            Image(
                painter = painterResource(id = core.designsystem.R.drawable.ic_check_green),
                contentDescription = "Check Icon",
            )
        },
        onDismiss = {},
    )
}
