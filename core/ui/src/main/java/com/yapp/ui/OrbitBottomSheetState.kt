package com.yapp.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.SoftwareKeyboardController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

typealias SheetContent = @Composable ColumnScope.() -> Unit

@Composable
fun rememberOrbitBottomSheetState(
    keyboardController: SoftwareKeyboardController?,
    scope: CoroutineScope,
): OrbitBottomSheetState {
    val bottomSheetContent: MutableState<SheetContent?> = remember {
        mutableStateOf(null)
    }
    val onHideBottomSheet = remember {
        mutableStateOf<() -> Unit>({})
    }

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = {
            if (it == ModalBottomSheetValue.Hidden) {
                onHideBottomSheet.value()
            }
            true
        },
        skipHalfExpanded = true,
    )

    return OrbitBottomSheetState(
        bottomSheetContent = bottomSheetContent,
        bottomSheetState = bottomSheetState,
        setOnHideBottomSheet = { onHideBottomSheet.value = it },
        keyboardController = keyboardController,
        scope = scope,
    )
}

@Stable
class OrbitBottomSheetState constructor(
    val bottomSheetContent: MutableState<SheetContent?>,
    val bottomSheetState: ModalBottomSheetState,
    val setOnHideBottomSheet: (() -> Unit) -> Unit,
    val keyboardController: SoftwareKeyboardController?,
    val scope: CoroutineScope,
) {
    val setBottomSheet: (SheetContent) -> Unit = { content: SheetContent ->
        bottomSheetContent.value = content
    }
    val clearBottomSheet: () -> Unit = {
        bottomSheetContent.value = null
    }

    fun showBottomSheet(content: SheetContent) = scope.launch {
        keyboardController?.hide()
        bottomSheetState.hide()
        setBottomSheet(content)
        bottomSheetState.show()
    }

    fun hideBottomSheet() = scope.launch {
        keyboardController?.hide()
        clearBottomSheet()
        bottomSheetState.hide()
    }
}
