package com.yapp.ui.extensions

import androidx.compose.foundation.Indication
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun Modifier.customClickable(
    rippleEnabled: Boolean = true,
    rippleColor: Color? = null,
    enabled: Boolean = true,
    onClick: (() -> Unit)?,
    onLongClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
): Modifier {
    return composed {
        val rippleIndication: Indication? = if (rippleEnabled) {
            ripple(color = rippleColor ?: Color.Unspecified, bounded = true)
        } else {
            null
        }

        this.then(
            Modifier
                .pointerInput(Unit) {
                    coroutineScope {
                        launch {
                            detectTapGestures(
                                onTap = { onClick?.invoke() },
                                onLongPress = { onLongClick?.invoke() },
                            )
                        }
                    }
                }
                .indication(interactionSource, rippleIndication),
        )
    }
}
