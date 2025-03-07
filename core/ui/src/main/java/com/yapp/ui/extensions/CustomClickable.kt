package com.yapp.ui.extensions

import androidx.compose.foundation.Indication
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun Modifier.customClickable(
    rippleEnabled: Boolean = true,
    rippleColor: Color? = null,
    enabled: Boolean = true,
    fadeOnPress: Boolean = false,
    pressedAlpha: Float = 0.5f,
    onClick: (() -> Unit)?,
    onLongClick: (() -> Unit)? = null,
    onPress: (() -> Unit)? = null,
    onRelease: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
): Modifier {
    return composed {
        val rippleIndication: Indication? = if (rippleEnabled) {
            ripple(color = rippleColor ?: Color.Unspecified, bounded = true)
        } else {
            null
        }

        var isPressed by remember { mutableStateOf(false) }

        this.then(
            Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isPressed = true
                            onPress?.invoke()
                            tryAwaitRelease()
                            isPressed = false
                            onRelease?.invoke()
                        },
                        onTap = { onClick?.invoke() },
                        onLongPress = { onLongClick?.invoke() },
                    )
                }
                .graphicsLayer {
                    if (fadeOnPress) {
                        alpha = if (isPressed) pressedAlpha else 1f
                    }
                }
                .indication(interactionSource, rippleIndication),
        )
    }
}
