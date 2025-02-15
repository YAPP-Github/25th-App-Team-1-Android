package com.yapp.ui.component.snackbar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OrbitSnackBar(
    modifier: Modifier = Modifier,
    label: String,
    message: String,
    @DrawableRes iconRes: Int? = null,
    shape: Shape = RoundedCornerShape(12.dp),
    containerColor: Color = OrbitTheme.colors.gray_600,
    onAction: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Surface(
            modifier = Modifier,
            shape = shape,
            color = containerColor,
            shadowElevation = 6.dp,
        ) {
            Row(
                modifier = Modifier.padding(
                    horizontal = 14.dp,
                    vertical = 12.dp,
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
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

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    modifier = Modifier
                        .clickable(
                            onClick = onAction,
                        ),
                    color = OrbitTheme.colors.gray_50,
                    style = OrbitTheme.typography.label2Regular,
                    text = label,
                )
            }
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

suspend fun showCustomSnackBar(
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    message: String,
    actionLabel: String? = null,
    iconRes: Int? = null,
    bottomPadding: Dp = 12.dp,
    durationMillis: Long = 2000L, // 지정된 duration 만큼 유지
    onDismiss: () -> Unit = {},
    onAction: () -> Unit = {},
): SnackbarResult {
    var result: SnackbarResult = SnackbarResult.Dismissed
    val job = scope.launch {
        result = snackBarHostState.showSnackbar(
            CustomSnackBarVisuals(
                message = message,
                actionLabel = actionLabel,
                iconRes = iconRes,
                bottomPadding = bottomPadding,
            ),
        )

        if (result == SnackbarResult.Dismissed) {
            onDismiss()
        } else if (result == SnackbarResult.ActionPerformed) {
            onAction()
        }
    }

    scope.launch {
        delay(durationMillis)
        if (job.isActive) {
            job.cancel()
            result = SnackbarResult.Dismissed
            onDismiss()
        }
    }

    job.join()
    return result
}

data class CustomSnackBarVisuals(
    override val message: String,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    val iconRes: Int? = null,
    val bottomPadding: Dp = 12.dp,
) : SnackbarVisuals
