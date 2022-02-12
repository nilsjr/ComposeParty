package de.nilsdruyen.composeparty.paths

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.Group
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun PawIcon(modifier: Modifier = Modifier) {
    val color = SolidColor(Color.White)
    val duration = 1000

    val parts = listOf(
        "M-0.88 -4.85 C-11.24,-1.32 -9.99,5.65 -11.9,12.35 C-12.79,15.43 -24.91,25.57 -17.64,35.27 C-11.13,43.95 4.34,33.27 8.82,32.85 C13.45,32.41 28,38.8 33.51,26.45 C38.45,15.38 24.69,11.9 19.4,7.72 C14.11,3.53 12.59,-9.43 -0.88,-4.85c",
        "M-43.25 -8 C-45.75,-5 -46,4.5 -41.75,10.25 C-37.5,16 -30.75,19.75 -23.5,17.75 C-16.25,15.75 -16.25,5.25 -22.75,-2.75 C-29.79,-11.41 -38.7,-13.46 -43.25,-8c",
        "M-20.75 -40.75 C-29,-39.25 -30.75,-26.25 -26.5,-17 C-22.25,-7.75 -13.73,-7.06 -8.5,-10.25 C-2.75,-13.75 -5.5,-26.75 -7.25,-30.5 C-9,-34.25 -12.5,-42.25 -20.75,-40.75c",
        "M-20.75 -40.75 C-29,-39.25 -30.75,-26.25 -26.5,-17 C-22.25,-7.75 -13.73,-7.06 -8.5,-10.25 C-2.75,-13.75 -5.5,-26.75 -7.25,-30.5 C-9,-34.25 -12.5,-42.25 -20.75,-40.75c",
        "M-43.25 -8 C-45.75,-5 -46,4.5 -41.75,10.25 C-37.5,16 -30.75,19.75 -23.5,17.75 C-16.25,15.75 -16.25,5.25 -22.75,-2.75 C-29.79,-11.41 -38.7,-13.46 -43.25,-8c",
    )

    val drawParts = remember { parts.map { addPathNodes(it) } }

    val infiniteTransition = rememberInfiniteTransition()

    val paw01 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = duration
                0.0f at 0
                1f at 300 with FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Reverse
        )
    )
    val paw02 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = duration
                0.0f at 200
                1f at 500 with FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Reverse
        )
    )
    val paw03 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = duration
                0.0f at 400
                1f at 700 with FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Reverse
        )
    )
    val paw04 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = duration
                0.0f at 600
                1f at 900 with FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    Image(
        modifier = Modifier
            .size(350.dp)
            .then(modifier),
        painter = rememberVectorPainter(
            defaultWidth = 104.dp,
            defaultHeight = 104.dp,
            viewportWidth = 104f,
            viewportHeight = 104f,
        ) { vw, vh ->
            Group {
                Group(
                    name = "01",
                    translationX = 52f,
                    translationY = 52f,
                ) {
                    Path(
                        pathData = drawParts[0],
                        stroke = color,
                        strokeLineWidth = 4f
                    )
                }
                Group(
                    name = "paw01",
                    scaleX = 0.959f * paw01,
                    scaleY = 1f * paw01,
                    translationX = 49.278f - 25 * (1 - paw01),
                    translationY = 50.879f - 5 * (1 - paw01),
                ) {
                    Path(
                        pathData = drawParts[1],
                        stroke = color,
                        strokeLineWidth = 4f,
                    )
                }
                Group(
                    name = "paw02",
                    scaleX = 0.94136f * paw02,
                    scaleY = 1f * paw02,
                    translationX = 50.5f,
                    translationY = 48.335f,
                ) {
                    Path(
                        pathData = drawParts[2],
                        stroke = color,
                        strokeLineWidth = 4f
                    )
                }
                Group(
                    name = "paw03",
                    rotation = -19.37f,
                    scaleX = -0.94136f * paw03,
                    scaleY = 1f * paw03,
                    translationX = 55.597f,
                    translationY = 48.182f,
                ) {
                    Path(
                        pathData = drawParts[3],
                        stroke = color,
                        strokeLineWidth = 4f
                    )
                }
                Group(
                    name = "paw04",
                    rotation = -25.753f,
                    scaleX = -0.9197799f * paw04,
                    scaleY = 1f * paw04,
                    translationX = 59.041f,
                    translationY = 53.054f,
                ) {
                    Path(
                        pathData = drawParts[4],
                        stroke = color,
                        pathFillType = PathFillType.NonZero,
                        strokeLineWidth = 4f,
                        strokeAlpha = 1f * paw04,
                        strokeLineCap = StrokeCap.Round,
                        strokeLineJoin = StrokeJoin.Round,
                    )
                }
            }
        },
        contentDescription = null,
    )
}