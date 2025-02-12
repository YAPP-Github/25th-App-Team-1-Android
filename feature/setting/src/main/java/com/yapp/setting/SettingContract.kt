package com.yapp.setting

import com.yapp.ui.base.UiState

sealed class SettingContract {
    data class State(
        val name: String = "테스트",
        val birthDate: String = "양력 1999년 7월 8일",
        val selectedGender: String? = null,
        val checkBoxState: Boolean = false,
        val isMaleSelected: Boolean = true,
        val isFemaleSelected: Boolean = false,
        val isTimeUnknown: Boolean = false,
        val timeOfBirth: String = "20:00",
        val isDialogVisible: Boolean = false,
        val isNameValid: Boolean = true,
        val isTimeValid: Boolean = true,
    ) : UiState

    sealed class Action {
        data object PreviousStep : Action()
        data class UpdateName(val name: String) : Action()
        data class UpdateBirthDate(val birthDate: String) : Action()
        data class UpdateGender(val gender: String) : Action()
        data class ToggleGender(val isMale: Boolean) : Action()
        data class ToggleTimeUnknown(val isChecked: Boolean) : Action()
        data class UpdateTimeOfBirth(val time: String) : Action()
        data object Reset : Action()

//        data object Submit : Action()
        data object NavigateToEditProfile : Action()
        data object NavigateToEditBirthday : Action()
        data object ShowDialog : Action()
        data object HideDialog : Action()
    }

    enum class FieldType(val validationRegex: Regex) {
        TIME(Regex("^(24:00|([0-1]\\d|2[0-3]):[0-5]\\d)\$")),
        NAME(Regex("^(?=.{1,13}\$)(?=.{1,6}(?:[가-힣]|[a-zA-Z]{2})\$)[가-힣a-zA-Z]*\$")),
    }

    sealed class SideEffect : com.yapp.ui.base.SideEffect {
        data class Navigate(
            val route: String,
            val popUpTo: String? = null,
            val inclusive: Boolean = false,
        ) : SideEffect()

        data object NavigateBack : SideEffect()
    }
}
