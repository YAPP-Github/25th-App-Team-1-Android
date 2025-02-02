package com.yapp.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.designsystem.theme.OrbitTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrbitBottomSheet(
    modifier: Modifier = Modifier,
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
            modifier = modifier,
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
    var isSheetOpen by rememberSaveable { mutableStateOf(true) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    OrbitTheme {
        Button(
            onClick = {
                scope.launch {
                    sheetState.show()
                }.invokeOnCompletion {
                    if (!isSheetOpen) {
                        isSheetOpen = true
                    }
                }
            },
        ) {
            Text("Toggle Bottom Sheet")
        }

        OrbitBottomSheet(
            isSheetOpen = isSheetOpen,
            sheetState = sheetState,
            onDismissRequest = { isSheetOpen = !isSheetOpen },
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (isSheetOpen) {
                                    isSheetOpen = false
                                }
                            }
                        },
                    ) {
                        Text("Toggle Bottom Sheet")
                    }
                }
            },
        )
    }
}
