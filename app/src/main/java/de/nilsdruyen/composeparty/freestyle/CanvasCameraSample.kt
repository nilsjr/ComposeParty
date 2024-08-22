package de.nilsdruyen.composeparty.freestyle

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@Composable
fun CanvasCameraSample(modifier: Modifier = Modifier) {
    Box(modifier.background(Color.Blue)) {
        Canvas(Modifier.fillMaxSize()) {
            drawIntoCanvas {
                drawRect(Color.Black)

                val (top, start) = this.size.width / 2 - 100 to this.size.height / 2 - 100
                val path = Path().apply {
                    moveTo(top, start)
                    lineTo(top + 180f, start)
                    lineTo(top + 180f, start + 180f)
                    lineTo(top, start + 180f)
                    close()
                }

                drawPath(path, Color.Transparent, blendMode = BlendMode.Clear)
                drawPath(path = path, color = Color.White, style = Stroke(width = 2f))
            }
        }
        Box(
            Modifier
                .size(200.dp)
                .align(Alignment.Center)
                .drawRainbowBorder(4.dp, 4000)
        )
    }
}

@Preview
@Composable
private fun CanvasCameraSamplePreview() {
    ComposePartyTheme {
        CanvasCameraSample(Modifier.fillMaxSize())
    }
}

val gradientColors = listOf(
    Color.Red,
    Color.Magenta,
    Color.Blue,
    Color.Cyan,
    Color.Green,
    Color.Yellow,
    Color.Red
)

fun Modifier.drawRainbowBorder(
    strokeWidth: Dp,
    durationMillis: Int
) = composed {

    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotation"
    )

    val brush = Brush.sweepGradient(gradientColors)

    Modifier.drawWithContent {

        val strokeWidthPx = strokeWidth.toPx()
        val width = size.width
        val height = size.height

        drawContent()

        with(drawContext.canvas.nativeCanvas) {
            val checkPoint = saveLayer(null, null)

            // Destination
            drawRoundRect(
                color = Color.Gray,
                topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2),
                size = Size(width - strokeWidthPx, height - strokeWidthPx),
                style = Stroke(strokeWidthPx),
                cornerRadius = CornerRadius(20f, 20f),
            )

            // Source
            rotate(angle) {

                drawCircle(
                    brush = brush,
                    radius = size.width,
                    blendMode = BlendMode.SrcIn,
                )
            }

            restoreToCount(checkPoint)
        }
    }
}