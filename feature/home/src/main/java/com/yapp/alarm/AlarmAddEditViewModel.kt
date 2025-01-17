package com.yapp.alarm

import androidx.lifecycle.viewModelScope
import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmAddEditViewModel @Inject constructor() : BaseViewModel<AlarmAddEditContract.State, AlarmAddEditContract.SideEffect>(
    initialState = AlarmAddEditContract.State(),
) {
    private var debounceJob: Job? = null // 디바운싱을 위한 Job

    fun processAction(action: AlarmAddEditContract.Action) {
        viewModelScope.launch {
            when (action) {
                is AlarmAddEditContract.Action.UpdateAlarmTime -> updateAlarmTime(action.amPm, action.hour, action.minute)
                is AlarmAddEditContract.Action.ToggleWeekdaysChecked -> toggleWeekdaysChecked()
                is AlarmAddEditContract.Action.ToggleWeekendsChecked -> toggleWeekendsChecked()
                is AlarmAddEditContract.Action.ToggleDaySelection -> toggleDaySelection(action.day)
                is AlarmAddEditContract.Action.ToggleDisableHolidayChecked -> toggleDisableHolidayChecked()
            }
        }
    }

    private suspend fun updateAlarmTime(amPm: String, hour: Int, minute: Int) {
        updateState {
            copy(
                currentAmPm = amPm,
                currentHour = hour,
                currentMinute = minute,
            )
        }
        debounceUpdateAlarmMessage()
    }

    private fun debounceUpdateAlarmMessage() {
        // 이전 Job 취소
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(200) // 200ms 대기
            updateAlarmMessage() // 알람 메시지 업데이트
        }
    }

    private fun updateAlarmMessage() {
        val now = java.time.LocalDateTime.now()

        // 설정된 알람 시간 계산
        val alarmHour = convertTo24HourFormat(currentState.currentAmPm, currentState.currentHour)
        val alarmTimeToday = now.toLocalDate().atTime(alarmHour, currentState.currentMinute)

        // 선택된 요일 중 다음 알람 시간이 언제인지 계산
        val nextAlarmDateTime = calculateNextAlarmDateTime(now, alarmTimeToday, currentState.selectedDays)

        // 남은 시간 계산
        val duration = java.time.Duration.between(now, nextAlarmDateTime)
        val totalMinutes = duration.toMinutes()
        val days = totalMinutes / (24 * 60)
        val hours = (totalMinutes % (24 * 60)) / 60
        val minutes = totalMinutes % 60

        // 출력 문구 생성
        val newMessage = when {
            days > 0 -> "${days}일 ${hours}시간 후에 울려요"
            hours > 0 -> "${hours}시간 ${minutes}분 후에 울려요"
            else -> "${minutes}분 후에 울려요"
        }

        updateState { copy(alarmMessage = newMessage) }
    }

    private fun toggleWeekdaysChecked() {
        val weekdays = setOf(AlarmDay.MON, AlarmDay.TUE, AlarmDay.WED, AlarmDay.THU, AlarmDay.FRI)
        val isChecked = !currentState.isWeekdaysChecked
        val updatedDays = if (isChecked) {
            currentState.selectedDays + weekdays
        } else {
            currentState.selectedDays - weekdays
        }

        updateState {
            copy(
                isWeekdaysChecked = isChecked,
                selectedDays = updatedDays,
            )
        }
        debounceUpdateAlarmMessage() // 디바운싱된 메시지 업데이트
    }

    private fun toggleWeekendsChecked() {
        val weekends = setOf(AlarmDay.SAT, AlarmDay.SUN)
        val isChecked = !currentState.isWeekendsChecked
        val updatedDays = if (isChecked) {
            currentState.selectedDays + weekends
        } else {
            currentState.selectedDays - weekends
        }

        updateState {
            copy(
                isWeekendsChecked = isChecked,
                selectedDays = updatedDays,
            )
        }
        debounceUpdateAlarmMessage() // 디바운싱된 메시지 업데이트
    }

    private fun toggleDaySelection(day: AlarmDay) {
        val updatedDays = if (day in currentState.selectedDays) {
            currentState.selectedDays - day
        } else {
            currentState.selectedDays + day
        }
        val weekdays = setOf(AlarmDay.MON, AlarmDay.TUE, AlarmDay.WED, AlarmDay.THU, AlarmDay.FRI)
        val weekends = setOf(AlarmDay.SAT, AlarmDay.SUN)

        updateState {
            copy(
                selectedDays = updatedDays,
                isWeekdaysChecked = updatedDays.containsAll(weekdays),
                isWeekendsChecked = updatedDays.containsAll(weekends),
            )
        }
        debounceUpdateAlarmMessage() // 디바운싱된 메시지 업데이트
    }

    private fun toggleDisableHolidayChecked() {
        updateState {
            copy(
                isDisableHolidayChecked = !currentState.isDisableHolidayChecked,
            )
        }
        debounceUpdateAlarmMessage() // 디바운싱된 메시지 업데이트
    }

    private fun convertTo24HourFormat(amPm: String, hour: Int): Int {
        return if (amPm == "오후" && hour != 12) {
            hour + 12
        } else if (amPm == "오전" && hour == 12) {
            0
        } else {
            hour
        }
    }

    private fun calculateNextAlarmDateTime(
        now: java.time.LocalDateTime,
        alarmTimeToday: java.time.LocalDateTime,
        selectedDays: Set<AlarmDay>,
    ): java.time.LocalDateTime {
        if (selectedDays.isEmpty()) {
            return if (alarmTimeToday.isBefore(now)) {
                alarmTimeToday.plusDays(1)
            } else {
                alarmTimeToday
            }
        }

        val currentDayOfWeek = now.dayOfWeek.value
        val selectedDaysOfWeek = selectedDays.map { it.toDayOfWeek().value }.sorted()
        val nextDay = selectedDaysOfWeek.firstOrNull { it > currentDayOfWeek }
            ?: selectedDaysOfWeek.first()
        val daysToAdd = if (nextDay > currentDayOfWeek) {
            nextDay - currentDayOfWeek
        } else {
            7 - (currentDayOfWeek - nextDay)
        }
        val nextAlarmDate = now.toLocalDate().plusDays(daysToAdd.toLong())
        return nextAlarmDate.atTime(alarmTimeToday.toLocalTime())
    }
}
