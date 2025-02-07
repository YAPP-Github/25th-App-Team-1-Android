package com.kms.onboarding

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.common.navigation.destination.OnboardingDestination
import com.yapp.domain.model.Alarm
import com.yapp.domain.model.AlarmDay
import com.yapp.domain.model.toRepeatDays
import com.yapp.domain.usecase.AlarmUseCase
import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val alarmUseCase: AlarmUseCase,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<OnboardingContract.State, OnboardingContract.SideEffect>(
    initialState = OnboardingContract.State(
        currentStep = savedStateHandle["currentStep"] ?: 1,
    ),
) {
    fun processAction(action: OnboardingContract.Action) {
        when (action) {
            is OnboardingContract.Action.NextStep -> moveToNextStep()
            is OnboardingContract.Action.PreviousStep -> moveToPreviousStep()
            is OnboardingContract.Action.SetAlarmTime -> setAlarmTime(action.isAm, action.hour, action.minute)
            is OnboardingContract.Action.CreateAlarm -> createAlarm()
            is OnboardingContract.Action.UpdateField -> updateField(action.value, action.fieldType)
            is OnboardingContract.Action.Reset -> resetFields()
            is OnboardingContract.Action.Submit -> handleSubmission(action.stepData)
            is OnboardingContract.Action.UpdateGender -> updateGender(action.gender)
            is OnboardingContract.Action.ToggleBottomSheet -> toggleBottomSheet()
        }
    }

    private fun moveToNextStep() {
        val currentStep = container.stateFlow.value.currentStep
        val nextStep = currentStep + 1
        val nextRoute = OnboardingDestination.nextRoute(currentStep)

        if (nextRoute != null) {
            savedStateHandle["currentStep"] = nextStep
            updateState { copy(currentStep = nextStep) }
            emitSideEffect(OnboardingContract.SideEffect.Navigate(nextRoute))
        } else {
            emitSideEffect(OnboardingContract.SideEffect.OnboardingCompleted)
        }
    }

    private fun moveToPreviousStep() {
        val currentStep = container.stateFlow.value.currentStep
        if (currentStep > 1) {
            val previousStep = currentStep - 1
            savedStateHandle["currentStep"] = previousStep
            updateState { copy(currentStep = previousStep) }
            emitSideEffect(OnboardingContract.SideEffect.NavigateBack)
        }
    }

    private fun setAlarmTime(amPm: String, hour: Int, minute: Int) {
        val newTimeState = currentState.timeState.copy(
            selectedAmPm = amPm,
            selectedHour = hour,
            selectedMinute = minute,
        )
        updateState {
            copy(
                timeState = newTimeState,
            )
        }
    }

    private fun createAlarm() {
        val alarm = Alarm(
            isAm = currentState.timeState.selectedAmPm == "오전",
            hour = currentState.timeState.selectedHour,
            minute = currentState.timeState.selectedMinute,
            repeatDays = setOf(AlarmDay.MON, AlarmDay.TUE, AlarmDay.WED, AlarmDay.THU, AlarmDay.FRI).toRepeatDays(),
        )

        viewModelScope.launch {
            alarmUseCase.insertAlarm(
                alarm = alarm,
            ).onSuccess {
                emitSideEffect(OnboardingContract.SideEffect.OnboardingCompleted)
            }.onFailure {
                Log.e("OnboardingViewModel", "Failed to create alarm", it)
            }
        }
    }

    private fun updateField(value: String, fieldType: OnboardingContract.FieldType) {
        val isValid = value.matches(fieldType.validationRegex)
        updateState {
            copy(
                textFieldValue = value,
                showWarning = value.isNotEmpty() && !isValid,
                isButtonEnabled = value.isNotEmpty() && isValid,
            )
        }
    }

    private fun resetFields() {
        updateState {
            copy(
                textFieldValue = "",
                showWarning = false,
                isButtonEnabled = false,
            )
        }
    }

    private fun updateGender(gender: String) {
        updateState { copy(selectedGender = gender, isButtonEnabled = true) }
    }

    private fun toggleBottomSheet() {
        val isCurrentlyOpen = container.stateFlow.value.isBottomSheetOpen
        updateState { copy(isBottomSheetOpen = !isCurrentlyOpen) }
    }

    private fun handleSubmission(stepData: Map<String, String>) {
        emitSideEffect(OnboardingContract.SideEffect.OnboardingCompleted)
    }
}
