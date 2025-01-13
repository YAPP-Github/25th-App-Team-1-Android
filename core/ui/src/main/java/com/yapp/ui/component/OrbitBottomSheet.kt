package com.yapp.ui.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrbitBottomSheet(
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    ),
    isSheetOpen: Boolean,
    onDismissRequest: () -> Unit = {},
    shape: Shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
    containerColor: Color = OrbitTheme.colors.gray_800,
    content: @Composable ColumnScope.() -> Unit,
) {
    val scope = rememberCoroutineScope()
    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            shape = shape,
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                    onDismissRequest()
                }
            },
            containerColor = containerColor,
            dragHandle = null,
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun OrbitBottomSheetPreview() {
    var isSheetOpen by rememberSaveable { mutableStateOf(false) }
    OrbitTheme {
        OrbitBottomSheet(
            isSheetOpen = true,
            onDismissRequest = { isSheetOpen = !isSheetOpen },
            content = {},
        )
    }
}
