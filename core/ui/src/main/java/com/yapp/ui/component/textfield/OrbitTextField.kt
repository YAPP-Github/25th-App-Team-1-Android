package com.yapp.ui.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme

@Composable
fun OrbitTextField(
    text: String,
    onTextChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    showWarning: Boolean = false,
    warningMessage: String,
    onFocusChanged: (Boolean) -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val customTextSelectionColors = TextSelectionColors(
        handleColor = OrbitTheme.colors.main,
        backgroundColor = OrbitTheme.colors.main,
    )

    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        Box(
            modifier = modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        focusManager.clearFocus()
                        onFocusChanged(false)
                    },
                ),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                TextFieldContainer(
                    text = text,
                    hint = hint,
                    showWarning = showWarning,
                    onTextChange = onTextChange,
                    onFocusChanged = onFocusChanged,
                    focusRequester = focusRequester,
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (showWarning) {
                    WarningMessage(warningMessage)
                }
            }
        }
    }
}

@Composable
private fun WarningMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            color = OrbitTheme.colors.alert,
            style = OrbitTheme.typography.label1SemiBold,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextFieldContainer(
    text: String,
    hint: String,
    showWarning: Boolean,
    onTextChange: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    focusRequester: FocusRequester,
) {
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .border(
                width = 3.dp,
                color = when {
                    isFocused && showWarning -> OrbitTheme.colors.alert
                    isFocused -> OrbitTheme.colors.main.copy(alpha = 0.2f)
                    showWarning -> OrbitTheme.colors.alert
                    else -> Color.Transparent
                },
                shape = RoundedCornerShape(10.dp),
            )
            .background(OrbitTheme.colors.gray_800, shape = RoundedCornerShape(10.dp))
            .height(52.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    focusRequester.requestFocus()
                    isFocused = true
                },
            ),
    ) {
        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            textStyle = TextStyle(
                color = if (text.isEmpty()) OrbitTheme.colors.gray_500 else OrbitTheme.colors.white,
                textAlign = TextAlign.Center,
            ),
            cursorBrush = SolidColor(OrbitTheme.colors.white),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = hint,
                            style = OrbitTheme.typography.body1Regular.copy(textAlign = TextAlign.Center),
                            color = OrbitTheme.colors.gray_500,
                        )
                    }
                    innerTextField()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    onFocusChanged(focusState.isFocused)
                },
        )

        if (text.isNotEmpty()) {
            Icon(
                painter = painterResource(id = core.designsystem.R.drawable.ic_circle_delete),
                contentDescription = "delete",
                tint = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 20.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onTextChange("") },
                    ),
            )
        }
    }
}

@Composable
@Preview
fun OrbitTextFieldPreview() {
    OrbitTheme {
        OrbitTextField(
            text = "123",
            onTextChange = {},
            showWarning = true,
            hint = "이름을 입력해주세요",
            warningMessage = "이름을 입력해주세요",
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}
