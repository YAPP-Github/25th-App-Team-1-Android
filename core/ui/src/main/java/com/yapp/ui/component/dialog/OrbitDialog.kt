package com.yapp.ui.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.component.button.OrbitButton

@Composable
fun OrbitDialog(
    title: String,
    message: String,
    confirmText: String,
    cancelText: String? = null,
    onConfirm: () -> Unit,
    onCancel: (() -> Unit)? = null,
    confirmButtonContainerColor: Color = OrbitTheme.colors.main,
    confirmButtonContentColor: Color = OrbitTheme.colors.gray_900,
    cancelButtonContainerColor: Color = OrbitTheme.colors.gray_600,
    cancelButtonContentColor: Color = OrbitTheme.colors.white,
    cornerRadius: Dp = 20.dp,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp

    Dialog(
        onDismissRequest = {
            if (cancelText == null) {
                onConfirm()
            } else {
                onCancel?.invoke()
            }
        },
    ) {
        Column(
            modifier = modifier
                .width(screenWidthDp - 64.dp)
                .background(
                    color = OrbitTheme.colors.gray_700,
                    shape = RoundedCornerShape(cornerRadius),
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                style = OrbitTheme.typography.headline1SemiBold,
                color = OrbitTheme.colors.gray_50,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = OrbitTheme.typography.body1Regular,
                color = OrbitTheme.colors.gray_300,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(20.dp))
            if (cancelText != null && onCancel != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    DialogButton(
                        text = cancelText,
                        containerColor = cancelButtonContainerColor,
                        contentColor = cancelButtonContentColor,
                        onClick = onCancel,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                    )
                    DialogButton(
                        text = confirmText,
                        containerColor = confirmButtonContainerColor,
                        contentColor = confirmButtonContentColor,
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                    )
                }
            } else {
                DialogButton(
                    text = confirmText,
                    containerColor = confirmButtonContainerColor,
                    contentColor = confirmButtonContentColor,
                    onClick = onConfirm,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun DialogButton(
    text: String,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        shape = RoundedCornerShape(16.dp),
    ) {
        Text(
            text = text,
            style = OrbitTheme.typography.body1SemiBold,
        )
    }
}

@Preview
@Composable
fun PreviewCustomDialogSingleButton() {
    var isDialogOpen by remember { mutableStateOf(true) }

    OrbitTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(OrbitTheme.colors.gray_900),
            contentAlignment = Alignment.Center,
        ) {
            OrbitButton(
                label = "알림창 열기",
                enabled = true,
                onClick = {
                    isDialogOpen = true
                },
            )

            if (isDialogOpen) {
                OrbitDialog(
                    title = "받은 운세가 없어요",
                    message = "알람이 울린 후 미션을 수행하면\n오늘의 운세를 받을 수 있어요.",
                    confirmText = "닫기",
                    onConfirm = {
                        isDialogOpen = false
                    },
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewCustomDialogDoubleButton() {
    var isDialogOpen by remember { mutableStateOf(true) }

    OrbitTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(OrbitTheme.colors.gray_900),
            contentAlignment = Alignment.Center,
        ) {
            OrbitButton(
                label = "알림창 열기",
                enabled = true,
                onClick = {
                    isDialogOpen = true
                },
            )

            if (isDialogOpen) {
                OrbitDialog(
                    title = "나가면 운세를 받을 수 없어요",
                    message = "미션을 수행하지 않고 나가시겠어요?",
                    confirmText = "나가기",
                    cancelText = "취소",
                    onConfirm = {},
                    onCancel = {
                        isDialogOpen = false
                    },
                )
            }
        }
    }
}
