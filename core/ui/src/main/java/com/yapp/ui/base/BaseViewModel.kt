package com.yapp.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

abstract class BaseViewModel<UI_STATE : UiState, SIDE_EFFECT : SideEffect>(
    initialState: UI_STATE,
) : ViewModel(), ContainerHost<UI_STATE, SIDE_EFFECT> {

    override val container = container<UI_STATE, SIDE_EFFECT>(initialState)

    /**
     * UI 상태 업데이트
     * @param reducer 현재 상태를 수정하는 람다식
     */
    protected fun updateState(reducer: UI_STATE.() -> UI_STATE) = intent {
        reduce { reducer(state) }
    }

    /**
     * 단일 부수 효과 전달
     * @param effect 전달할 부수 효과
     */
    protected fun emitSideEffect(effect: SIDE_EFFECT) = intent {
        postSideEffect(effect)
    }

    /**
     * 여러 부수 효과 전달
     * @param effects 전달할 부수 효과 리스트
     */
    protected fun emitSideEffects(vararg effects: SIDE_EFFECT) = intent {
        effects.forEach { postSideEffect(it) }
    }

    /**
     * Flow 구독하고 상태 업데이트 or 부수 효과 처리
     * @param flow 구독할 Flow
     * @param onEach 각 데이터 처리 로직
     * @param onError 에러 처리 로직
     */
    protected fun <T> collectFlow(
        flow: Flow<T>,
        onEach: (T) -> Unit,
        onError: ((Throwable) -> Unit)? = null,
    ) = intent {
        flow.catch { onError?.invoke(it) }
            .collect { onEach(it) }
    }

    /**
     * 비동기 작업 수행하고 상태 업데이트 or 부수 효과 처리
     * @param block 실행할 suspend 블록
     * @param onError 에러 처리 로직 (옵션)
     */
    protected fun launchWithErrorHandler(
        block: suspend () -> Unit,
        onError: ((Throwable) -> Unit)? = null,
    ) = intent {
        kotlin.runCatching {
            block()
        }.onFailure { onError?.invoke(it) }
    }
}
