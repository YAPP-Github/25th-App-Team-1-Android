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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme

@Composable
fun OrbitTextField(
    text: String,
    onTextChange: (String) -> Unit,
    maxLength: Int,
    modifier: Modifier = Modifier,
    showWarning: Boolean = false,
    onFocusChanged: (Boolean) -> Unit = {},
    onValidationStateChanged: (isValid: Boolean, isTooLong: Boolean) -> Unit = { _, _ -> },
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
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        focusManager.clearFocus()
                        onFocusChanged(false)
                    },
                ),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                TextFieldContainer(
                    text = text,
                    maxLength = maxLength,
                    showWarning = showWarning,
                    onTextChange = { input ->
                        onTextChange(input)
                        val isValid = validateText(input)
                        val isTooLong = input.length > maxLength
                        onValidationStateChanged(isValid, isTooLong)
                    },
                    onFocusChanged = onFocusChanged,
                    focusRequester = focusRequester,
                )

                Spacer(modifier = Modifier.height(12.dp))

                WarningMessage(
                    text = text,
                    maxLength = maxLength,
                    showWarning = showWarning,
                )
            }
        }
    }
}

@Composable
private fun TextFieldContainer(
    text: String,
    maxLength: Int,
    showWarning: Boolean,
    onTextChange: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    focusRequester: FocusRequester,
) {
    var isFocused by remember { mutableStateOf(false) }
    val isTextValid = validateText(text)
    val isTextTooLong = text.length > maxLength

    Box(
        modifier = Modifier
            .border(
                width = 3.dp,
                color = when {
                    isFocused && !isTextValid && text.isNotEmpty() -> OrbitTheme.colors.alert
                    isFocused -> OrbitTheme.colors.main.copy(alpha = 0.2f)
                    isTextTooLong -> OrbitTheme.colors.alert
                    showWarning && !isTextValid && text.isNotEmpty() -> OrbitTheme.colors.alert
                    showWarning && text.isEmpty() -> OrbitTheme.colors.main.copy(alpha = 0.2f)
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
        contentAlignment = Alignment.Center,
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
                            text = "PlaceHolder",
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
    }
}

@Composable
private fun WarningMessage(
    text: String,
    maxLength: Int,
    showWarning: Boolean,
) {
    val isTextValid = validateText(text)
    val isTextTooLong = text.length > maxLength

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (showWarning && (!isTextValid || isTextTooLong)) {
            Text(
                text = "유효성 검사에 걸림",
                color = OrbitTheme.colors.alert,
                style = OrbitTheme.typography.label1SemiBold,
            )
        }
    }
}

private fun validateText(text: String): Boolean {
    val textWithoutSpaces = text.replace("\\s".toRegex(), "")
    return textWithoutSpaces.matches(Regex("^[a-zA-Z가-힣0-9ㄱ-ㅎㅏ-ㅣ가-힣\\W]{2,50}$"))
}

@Composable
@Preview
fun OrbitTextFieldPreview() {
    OrbitTheme {
        OrbitTextField(
            text = "",
            onTextChange = {},
            maxLength = 50,
            showWarning = true,
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}
