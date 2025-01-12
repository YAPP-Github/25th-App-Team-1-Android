package com.kms.onboarding

import com.yapp.ui.base.UiState

sealed class OnboardingContract {

    data class State(
        val currentStep: Int = 1,
        val textFieldValue: String = "",
        val showWarning: Boolean = false,
        val isButtonEnabled: Boolean = false,
        val selectedGender: String? = null,
        val isBottomSheetOpen: Boolean = false,
    ) : UiState

    sealed class Action {
        data object NextStep : Action()
        data object PreviousStep : Action()
        data class UpdateField(val value: String, val fieldType: FieldType) : Action()
        data object Reset : Action()
        data class Submit(val stepData: Map<String, String>) : Action()
        data class UpdateGender(val gender: String) : Action()
        data object ToggleBottomSheet : Action()
    }

    enum class FieldType(val validationRegex: Regex) {
        TIME(Regex("^\\d{2}:\\d{2}\$")),
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
