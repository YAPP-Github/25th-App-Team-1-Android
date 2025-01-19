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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme

@Composable
fun OrbitTextField(
    text: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    showWarning: Boolean = false,
    warningMessage: String,
    onFocusChanged: (Boolean) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
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
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = if (showWarning) OrbitTheme.colors.alert else OrbitTheme.colors.gray_500,
                            shape = RoundedCornerShape(16.dp),
                        )
                        .background(OrbitTheme.colors.gray_800, shape = RoundedCornerShape(16.dp))
                        .height(54.dp)
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                focusRequester.requestFocus()
                            },
                        ),
                ) {
                    BasicTextField(
                        value = text,
                        onValueChange = { newValue ->
                            val updatedValue = newValue.copy(selection = TextRange(newValue.text.length))
                            onTextChange(updatedValue)
                        },
                        textStyle = TextStyle(
                            color = if (text.text.isEmpty()) OrbitTheme.colors.gray_500 else OrbitTheme.colors.white,
                            textAlign = TextAlign.Center,
                        ),
                        keyboardOptions = keyboardOptions,
                        cursorBrush = SolidColor(OrbitTheme.colors.white),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                if (text.text.isEmpty()) {
                                    Text(
                                        text = hint,
                                        style = OrbitTheme.typography.body1Regular.copy(textAlign = TextAlign.Center),
                                        color = OrbitTheme.colors.gray_500,
                                    )
                                }
                                innerTextField()
                            }
                        },
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (showWarning) {
                    WarningMessage(warningMessage)
                }
            }
        }
    }
}

@Composable
fun OrbitTextField(
    text: String,
    onTextChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    showWarning: Boolean = false,
    warningMessage: String,
    onFocusChanged: (Boolean) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    OrbitTextFieldBase(
        value = text,
        onValueChange = onTextChange,
        hint = hint,
        modifier = modifier,
        showWarning = showWarning,
        warningMessage = warningMessage,
        onFocusChanged = onFocusChanged,
        keyboardOptions = keyboardOptions,
    )
}

@Composable
private fun OrbitTextFieldBase(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier,
    showWarning: Boolean,
    warningMessage: String,
    onFocusChanged: (Boolean) -> Unit,
    keyboardOptions: KeyboardOptions,
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
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = if (showWarning) OrbitTheme.colors.alert else OrbitTheme.colors.gray_500,
                            shape = RoundedCornerShape(16.dp),
                        )
                        .background(OrbitTheme.colors.gray_800, shape = RoundedCornerShape(16.dp))
                        .height(54.dp)
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                focusRequester.requestFocus()
                            },
                        ),
                ) {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        textStyle = TextStyle(
                            color = if (value.isEmpty()) OrbitTheme.colors.gray_500 else OrbitTheme.colors.white,
                            textAlign = TextAlign.Center,
                        ),
                        keyboardOptions = keyboardOptions,
                        cursorBrush = SolidColor(OrbitTheme.colors.white),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                if (value.isEmpty()) {
                                    Text(
                                        text = hint,
                                        style = OrbitTheme.typography.body1Regular.copy(textAlign = TextAlign.Center),
                                        color = OrbitTheme.colors.gray_500,
                                    )
                                }
                                innerTextField()
                            }
                        },
                    )
                }

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
            style = OrbitTheme.typography.label2Regular,
        )
    }
}

@Composable
@Preview
fun OrbitTextFieldPreview() {
    OrbitTheme {
        OrbitTextField(
            text = TextFieldValue(""),
            onTextChange = {},
            showWarning = false,
            hint = "이름을 입력해주세요",
            warningMessage = "이름을 입력해주세요",
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}
