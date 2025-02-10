package com.yapp.alarm.interaction.snooze

import androidx.lifecycle.viewModelScope
import com.yapp.common.navigation.Routes
import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class AlarmSnoozeTimerViewModel @Inject constructor() : BaseViewModel<AlarmSnoozeTimerContract.State, AlarmSnoozeTimerContract.SideEffect>(
    AlarmSnoozeTimerContract.State(),
) {
    init {
        startClock()
    }

    private fun startClock() {
        viewModelScope.launch {
            updateState {
                copy(
                    alarmTimeStamp = System.currentTimeMillis() / 1000 + 300,
                    totalSeconds = 300,
                )
            }.join()

            while (true) {
                val currentTime = System.currentTimeMillis() / 1000
                val remaining = max(0, currentState.alarmTimeStamp - currentTime)

                updateState {
                    copy(
                        remainingSeconds = remaining.toInt(),
                        initialLoading = false,
                    )
                }

                delay(1000L)
            }
        }
    }

    fun processAction(action: AlarmSnoozeTimerContract.Action) {
        when (action) {
            is AlarmSnoozeTimerContract.Action.Dismiss -> dismiss()
        }
    }

    private fun dismiss() {
        emitSideEffect(AlarmSnoozeTimerContract.SideEffect.Navigate(Routes.Mission.ROUTE))
    }
}
