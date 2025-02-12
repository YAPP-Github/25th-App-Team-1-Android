package com.yapp.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.setting.component.SettingTopAppBar
import com.yapp.ui.component.checkbox.OrbitCheckBox
import com.yapp.ui.component.dialog.OrbitDialog
import com.yapp.ui.component.textfield.OrbitTextField
import com.yapp.ui.component.textfield.WarningMessage
import com.yapp.ui.toggle.OrbitGenderToggle

@Composable
fun EditProfileRoute(
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val state by viewModel.container.stateFlow.collectAsStateWithLifecycle()

    EditProfileScreen(
        state = state,
        onBack = { viewModel.onAction(SettingContract.Action.ShowDialog) },
        onUpdateName = { name -> viewModel.onAction(SettingContract.Action.UpdateName(name)) },
        onUpdateBirthDate = { birthDate ->
            viewModel.onAction(
                SettingContract.Action.UpdateBirthDate(
                    birthDate,
                ),
            )
        },
        onToggleGender = { isMale -> viewModel.onAction(SettingContract.Action.ToggleGender(isMale)) },
        onToggleTimeUnknown = { isChecked ->
            viewModel.onAction(
                SettingContract.Action.ToggleTimeUnknown(
                    isChecked,
                ),
            )
        },
        onUpdateTimeOfBirth = { time ->
            viewModel.onAction(
                SettingContract.Action.UpdateTimeOfBirth(
                    time,
                ),
            )
        },
        onNavigateToEditBirthday = {
            viewModel.onAction(
                SettingContract.Action.NavigateToEditBirthday,
            )
        },
        onConfirmExit = { viewModel.onAction(SettingContract.Action.PreviousStep) },
        onCancelDialog = { viewModel.onAction(SettingContract.Action.HideDialog) },
    )
}

@Composable
fun EditProfileScreen(
    state: SettingContract.State,
    onBack: () -> Unit,
    onUpdateName: (String) -> Unit,
    onUpdateBirthDate: (String) -> Unit,
    onToggleGender: (Boolean) -> Unit,
    onToggleTimeUnknown: (Boolean) -> Unit,
    onUpdateTimeOfBirth: (String) -> Unit,
    onNavigateToEditBirthday: () -> Unit,
    onConfirmExit: () -> Unit,
    onCancelDialog: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    val nameTextFieldValue = remember(state.name) {
        TextFieldValue(
            text = state.name,
            selection = TextRange(state.name.length),
        )
    }
    val birthTimeTextFieldValue = remember(state.timeOfBirth) {
        TextFieldValue(
            text = state.timeOfBirth,
            selection = TextRange(state.timeOfBirth.length),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OrbitTheme.colors.gray_900)
            .imePadding()
            .navigationBarsPadding()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { focusManager.clearFocus() },
            ),
    ) {
        SettingTopAppBar(
            onBackClick = onBack,
            showTopAppBarActions = true,
            title = "프로필 수정",
            actionTitle = "저장",
        )
        ContentsTitle(
            contentsTitle = "이름",
            modifier = Modifier.padding(horizontal = 20.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        OrbitTextField(
            text = nameTextFieldValue,
            onTextChange = { newValue ->
                onUpdateName(newValue.text)
            },
            hint = "이름 입력",
            isValid = state.isNameValid,
            showWarning = !state.isNameValid,
            warningMessage = "올바른 이름을 입력해주세요.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            textAlign = TextAlign.Start,
        )
        Spacer(modifier = Modifier.height(18.dp))
        ContentsTitle(
            contentsTitle = "생년월일",
            modifier = Modifier.padding(horizontal = 20.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        BirthCard(
            birthDate = state.birthDate,
            onUpdateBirthDate = onUpdateBirthDate,
            onNavigateToEditBirthday = onNavigateToEditBirthday,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        )
        Spacer(modifier = Modifier.height(24.dp))
        ContentsTitle(
            contentsTitle = "성별",
            modifier = Modifier.padding(horizontal = 20.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(modifier = Modifier.weight(1f)) {
                OrbitGenderToggle(
                    label = "남성",
                    isSelected = state.isMaleSelected,
                    onToggle = { onToggleGender(true) },
                    height = 52.dp,
                    textStyle = OrbitTheme.typography.body1Regular,
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                OrbitGenderToggle(
                    label = "여성",
                    isSelected = state.isFemaleSelected,
                    onToggle = { onToggleGender(false) },
                    height = 52.dp,
                    textStyle = OrbitTheme.typography.body1Regular,
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        ContentsTitle(
            contentsTitle = "태어난 시간",
            modifier = Modifier.padding(horizontal = 20.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 18.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OrbitTextField(
                    text = birthTimeTextFieldValue,
                    onTextChange = { newValue ->
                        val formattedTime = formatTimeInput(newValue.text, state.timeOfBirth)
                        onUpdateTimeOfBirth(formattedTime)
                    },
                    hint = "시간 입력",
                    isValid = state.isTimeValid,
                    showWarning = false,
                    warningMessage = "",
                    enabled = !state.isTimeUnknown,
                    modifier = Modifier
                        .weight(1f),
                    textAlign = TextAlign.Start,
                )
                Spacer(modifier = Modifier.width(12.dp))

                OrbitCheckBox(
                    checked = state.isTimeUnknown,
                    onCheckedChange = {
                        onToggleTimeUnknown(!state.isTimeUnknown)
                        if (!state.isTimeUnknown) {
                            onUpdateTimeOfBirth("시간모름")
                        } else {
                            onUpdateTimeOfBirth("")
                        }
                    },
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "시간모름",
                    style = OrbitTheme.typography.body1Medium,
                    color = if (state.isTimeUnknown) OrbitTheme.colors.main else OrbitTheme.colors.white,
                )
            }
            if (!state.isTimeValid) {
                WarningMessage(
                    message = "올바른 시간을 입력해주세요.",
                    textAlign = TextAlign.Start,
                )
            }
        }
    }
    if (state.isDialogVisible) {
        OrbitDialog(
            title = "변경 사항 삭제",
            message = "변경 사항을 저장하지 않고\n나가시겠어요?",
            confirmText = "나가기",
            cancelText = "취소",
            onConfirm = onConfirmExit,
            onCancel = onCancelDialog,
        )
    }
}

fun formatTimeInput(input: String, previousText: String): String {
    val sanitizedValue = input.filter { it.isDigit() }
    val isDeleting = sanitizedValue.length < previousText.filter { it.isDigit() }.length

    return when {
        isDeleting && previousText.endsWith(":") -> sanitizedValue
        sanitizedValue.length > 2 -> {
            val hours = sanitizedValue.take(2)
            val minutes = sanitizedValue.drop(2).take(2)
            "$hours:$minutes"
        }
        sanitizedValue.length == 2 -> {
            if (previousText.length == 3 && previousText.endsWith(":")) {
                sanitizedValue
            } else {
                "$sanitizedValue:"
            }
        }
        else -> sanitizedValue
    }
}

@Composable
private fun ContentsTitle(
    contentsTitle: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = contentsTitle,
        modifier = modifier.fillMaxWidth(),
        style = OrbitTheme.typography.body1Medium,
        color = OrbitTheme.colors.white,
    )
}

@Composable
private fun BirthCard(
    modifier: Modifier = Modifier,
    birthDate: String,
    onUpdateBirthDate: (String) -> Unit,
    onNavigateToEditBirthday: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .background(
                color = OrbitTheme.colors.gray_800,
                shape = RoundedCornerShape(12.dp),
            )
            .border(
                width = 1.dp,
                color = OrbitTheme.colors.gray_700,
                shape = RoundedCornerShape(12.dp),
            )
            .clickable { onNavigateToEditBirthday() },
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
    ) {
        Text(
            text = birthDate,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = OrbitTheme.typography.body1Regular,
            color = OrbitTheme.colors.gray_50,
        )
    }
}

@Composable
@Preview
fun EditProfileScreenPreview() {
    EditProfileScreen(
        state = SettingContract.State(),
        onBack = {},
        onToggleGender = {},
        onToggleTimeUnknown = {},
        onUpdateTimeOfBirth = {},
        onUpdateName = {},
        onUpdateBirthDate = {},
        onNavigateToEditBirthday = {},
        onConfirmExit = {},
        onCancelDialog = {},
    )
}
