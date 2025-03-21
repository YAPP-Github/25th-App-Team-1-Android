package com.yapp.mission

import android.app.Application
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yapp.alarm.pendingIntent.interaction.createAlarmDismissIntent
import com.yapp.analytics.AnalyticsEvent
import com.yapp.analytics.AnalyticsHelper
import com.yapp.common.navigation.destination.FortuneDestination
import com.yapp.common.navigation.destination.HomeDestination
import com.yapp.common.navigation.destination.MissionDestination
import com.yapp.datastore.UserPreferences
import com.yapp.domain.repository.FortuneRepository
import com.yapp.media.haptic.HapticFeedbackManager
import com.yapp.media.haptic.HapticType
import com.yapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MissionViewModel @Inject constructor(
    private val analyticsHelper: AnalyticsHelper,
    private val hapticFeedbackManager: HapticFeedbackManager,
    private val fortuneRepository: FortuneRepository,
    private val userPreferences: UserPreferences,
    private val app: Application,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<MissionContract.State, MissionContract.SideEffect>(
    MissionContract.State(),
) {
    init {
        val notificationId = savedStateHandle.get<String>("notificationId")?.toLong()
        if (notificationId != null) {
            sendAlarmDismissIntent(notificationId)
        }
    }

    fun processAction(action: MissionContract.Action) {
        when (action) {
            is MissionContract.Action.NextStep -> {
                emitSideEffect(
                    MissionContract.SideEffect.Navigate(route = MissionDestination.Progress.route),
                )
            }

            is MissionContract.Action.PreviousStep -> {
                emitSideEffect(MissionContract.SideEffect.NavigateBack)
            }

            is MissionContract.Action.StartOverlayTimer -> startOverlayTimer()

            is MissionContract.Action.ShakeCard, is MissionContract.Action.ClickCard -> handleIncreaseCount()

            is MissionContract.Action.ShowExitDialog -> updateState { copy(showExitDialog = true) }
            is MissionContract.Action.HideExitDialog -> updateState { copy(showExitDialog = false) }
            is MissionContract.Action.RetryPostFortune -> retryPostFortune()
        }
    }

    private fun handleIncreaseCount() = viewModelScope.launch {
        if (currentState.showOverlay) updateState { copy(showOverlay = false) }
        if (currentState.showOverlayText) updateState { copy(showOverlayText = false) }

        val currentCount = currentState.shakeCount
        if (currentCount < 9) {
            hapticFeedbackManager.performHapticFeedback(HapticType.SUCCESS)
            updateState { copy(shakeCount = currentCount + 1) }
        } else if (currentCount == 9 && !currentState.isFlipped) {
            hapticFeedbackManager.performHapticFeedback(HapticType.SUCCESS)
            analyticsHelper.logEvent(
                AnalyticsEvent(
                    type = "mission_success",
                    properties = mapOf(
                        AnalyticsEvent.MissionPropertiesKeys.MISSION_TYPE to "shake",
                    ),
                ),
            )
            postFortune()
            updateState {
                copy(
                    isMissionCompleted = true,
                    shakeCount = 10,
                    isFlipped = true,
                )
            }
            kotlinx.coroutines.delay(500)
        }
    }

    private fun postFortune() {
        viewModelScope.launch {
            val userId = userPreferences.userIdFlow.firstOrNull() ?: return@launch
            val fortuneResult = runCatching {
                withContext(Dispatchers.IO) {
                    fortuneRepository.postFortune(userId)
                }
            }
            fortuneResult.onSuccess { fortune ->
                val fortuneData = fortune.getOrThrow()
                userPreferences.saveFortuneId(fortuneData.id)
                userPreferences.saveFortuneScore(fortuneData.avgFortuneScore)

                emitSideEffect(
                    MissionContract.SideEffect.Navigate(
                        route = FortuneDestination.Route.route,
                        popUpTo = MissionDestination.Route.route,
                        inclusive = true,
                    ),
                )
            }.onFailure { error ->
                Log.e("MissionViewModel", "운세 데이터 요청 실패: ${error.message}")
                updateState { copy(errorMessage = error.message) }
            }
        }
    }

    private fun retryPostFortune() {
        viewModelScope.launch {
            val userId = userPreferences.userIdFlow.firstOrNull() ?: return@launch
            val fortuneResult = runCatching {
                withContext(Dispatchers.IO) {
                    fortuneRepository.postFortune(userId)
                }
            }

            fortuneResult.onSuccess { fortune ->
                val fortuneData = fortune.getOrThrow()
                userPreferences.saveFortuneId(fortuneData.id)
                userPreferences.saveFortuneScore(fortuneData.avgFortuneScore)

                emitSideEffect(
                    MissionContract.SideEffect.Navigate(
                        route = FortuneDestination.Route.route,
                        popUpTo = MissionDestination.Route.route,
                        inclusive = true,
                    ),
                )
            }.onFailure {
                Log.e("MissionViewModel", "운세 데이터 재요청 실패: ${it.message}")
                navigateToHome()
            }
        }
    }

    private fun navigateToHome() {
        emitSideEffect(
            MissionContract.SideEffect.Navigate(
                route = HomeDestination.Route.route,
                popUpTo = MissionDestination.Route.route,
                inclusive = true,
            ),
        )
    }

    private fun startOverlayTimer() = viewModelScope.launch {
        updateState { copy(showOverlay = true) }
        kotlinx.coroutines.delay(1000)
        updateState { copy(showOverlayText = true) }
        kotlinx.coroutines.delay(2000)
        updateState { copy(showOverlay = false, showOverlayText = false) }
    }

    private fun sendAlarmDismissIntent(id: Long) {
        val alarmDismissIntent = createAlarmDismissIntent(
            context = app,
            notificationId = id,
        )
        app.sendBroadcast(alarmDismissIntent)
    }
}
