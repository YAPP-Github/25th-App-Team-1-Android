package com.yapp.ui.component.tooltip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.yapp.designsystem.theme.OrbitTheme
import com.yapp.ui.utils.toPx
import com.yapp.ui.utils.toRoundPx

@Composable
fun OrbitToolTip(
    backgroundColor: Color = OrbitTheme.colors.gray_900,
    textColor: Color = OrbitTheme.colors.gray_100,
    textStyle: TextStyle = OrbitTheme.typography.label2SemiBold,
    offset: IntOffset = IntOffset(0, 0),
    text: String,
) {
    val popupOffset = IntOffset(
        x = offset.x + 15.dp.toRoundPx(),
        y = offset.y + 2.dp.toRoundPx(),
    )

    Popup(
        alignment = Alignment.Center,
        offset = popupOffset,
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
        ) {
            Row {
                Spacer(modifier = Modifier.width(15.dp))

                Spacer(
                    modifier = Modifier
                        .width(10.dp)
                        .height(5.dp)
                        .background(
                            color = backgroundColor,
                            shape = RoundedTopTriangleShape(1.dp.toPx()),
                        ),
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = backgroundColor,
            ) {
                Text(
                    modifier = Modifier.padding(
                        vertical = 6.dp,
                        horizontal = 10.dp,
                    ),
                    text = text,
                    style = textStyle,
                    color = textColor,
                )
            }
        }
    }
}

class RoundedTopTriangleShape(private val radius: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val path = Path().apply {
            val topX = size.width / 2f
            val topY = 0f
            val bottomLeftX = 0f
            val bottomLeftY = size.height
            val bottomRightX = size.width
            val bottomRightY = size.height

            // 왼쪽 아래 꼭짓점 시작
            moveTo(bottomLeftX, bottomLeftY)

            lineTo(topX - radius, topY)

            arcTo(
                rect = Rect(
                    left = topX - radius,
                    top = topY,
                    right = topX + radius,
                    bottom = topY + (2 * radius),
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 180f,
                forceMoveTo = false,
            )

            lineTo(bottomRightX, bottomRightY)

            close()
        }

        return Outline.Generic(path)
    }
}

@Preview
@Composable
fun ReversedPopup() {
    OrbitTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray),
        ) {
            Box(
                modifier = Modifier.align(Alignment.Center),
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .clickable {},
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(id = core.designsystem.R.drawable.ic_mail),
                        contentDescription = "Mail",
                        tint = OrbitTheme.colors.white,
                    )
                }

                OrbitToolTip(
                    text = "운세 도착",
                    offset = IntOffset(x = 0, y = 32.dp.toRoundPx()),
                )
            }
        }
    }
}
