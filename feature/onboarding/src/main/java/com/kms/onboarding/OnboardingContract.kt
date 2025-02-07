package com.kms.onboarding

import com.yapp.ui.base.UiState

sealed class OnboardingContract {

    data class State(
        val currentStep: Int = 1,
        val timeState: AlarmTimeState = AlarmTimeState(),
        val textFieldValue: String = "",
        val showWarning: Boolean = false,
        val isButtonEnabled: Boolean = false,
        val selectedGender: String? = null,
        val isBottomSheetOpen: Boolean = false,
    ) : UiState

    data class AlarmTimeState(
        val selectedAmPm: String = "오전",
        val selectedHour: Int = 1,
        val selectedMinute: Int = 0,
    )

    sealed class Action {
        data object NextStep : Action()
        data object PreviousStep : Action()
        data class SetAlarmTime(val isAm: String, val hour: Int, val minute: Int) : Action()
        data object CreateAlarm : Action()
        data class UpdateField(val value: String, val fieldType: FieldType) : Action()
        data object Reset : Action()
        data class Submit(val stepData: Map<String, String>) : Action()
        data class UpdateGender(val gender: String) : Action()
        data object ToggleBottomSheet : Action()
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
        data object OnboardingCompleted : SideEffect()
        data object ResetField : SideEffect()
    }
}
