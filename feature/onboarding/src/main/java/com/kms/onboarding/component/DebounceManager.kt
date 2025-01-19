package com.kms.onboarding.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce

interface DebounceManager {
    fun debounceClick(action: () -> Unit)
}

@OptIn(FlowPreview::class)
@Composable
fun DebounceManager(
    debounceInterval: Long = 200L,
    content: @Composable (DebounceManager) -> Unit,
) {
    val debounceFlow = remember {
        MutableSharedFlow<() -> Unit>(
            replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )
    }

    LaunchedEffect(Unit) {
        debounceFlow
            .debounce(debounceInterval)
            .collect { it() }
    }

    content(
        object : DebounceManager {
            override fun debounceClick(action: () -> Unit) {
                debounceFlow.tryEmit(action)
            }
        },
    )
}
