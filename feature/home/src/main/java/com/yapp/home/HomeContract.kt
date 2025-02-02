package com.yapp.home

import com.yapp.domain.model.Alarm
import com.yapp.domain.model.AlarmDay
import com.yapp.domain.model.toRepeatDays
import com.yapp.ui.base.UiState

sealed class HomeContract {

    data class State(
        val alarms: List<Alarm> = listOf(
            Alarm(
                repeatDays = listOf(AlarmDay.MON, AlarmDay.TUE, AlarmDay.WED, AlarmDay.FRI).toRepeatDays(),
            ),
            Alarm(
                repeatDays = listOf(AlarmDay.SUN, AlarmDay.SAT).toRepeatDays(),
            ),
            Alarm(
                repeatDays = listOf(AlarmDay.MON, AlarmDay.TUE, AlarmDay.WED, AlarmDay.THU, AlarmDay.FRI).toRepeatDays(),
            ),
            Alarm(
                repeatDays = listOf(AlarmDay.SUN, AlarmDay.SAT).toRepeatDays(),
            ),
            Alarm(
                repeatDays = listOf(AlarmDay.WED, AlarmDay.THU).toRepeatDays(),
            ),
        ),
        val lastFortuneScore: Int = 50,
        val deliveryTime: String = "2025-02-01T22:00",
        val name: String = "동현",
    ) : UiState

    sealed class Action {
        data object NavigateToAlarmAdd : Action()
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
