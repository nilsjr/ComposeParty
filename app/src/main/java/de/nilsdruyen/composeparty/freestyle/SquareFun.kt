package de.nilsdruyen.composeparty.freestyle

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultBlendMode
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import kotlin.math.pow

@Composable
fun SquareFun(modifier: Modifier = Modifier) {
    val path = remember { Path() }
    val animatedProgress = remember { Animatable(0f, 0f) }
    var isDone = false
    var aColor: AColor = AColor.WHITE
    val paint = remember { Paint() }

    LaunchedEffect(Unit) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2500, easing = LinearEasing)
            )
        )
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
    ) {
        val t = animatedProgress.value
        val w = size.width
        val h = size.height
        val d = w * 0.28f
        val centerX = w / 2
        val centerY = h / 2

        if (t < 0.5) {
            if (isDone) aColor = aColor.invert()
            isDone = false
            val tt = map(t, 0f, 0.5f, 0f, 1f)

            drawRect(
                color = aColor.getColor(),
                topLeft = Offset(
                    0f,
                    h / 2 - w / 2
                ),
                size = Size(w, w)
            )

            val p = easing1(tt)
            val o = progress(p, w, d * 2)
            drawRect(
                color = aColor.invert().getColor(),
                topLeft = Offset(
                    progress(p, 0f, w / 2 - d),
                    progress(p, h / 2 - centerX, h / 2 - d)
                ),
                size = Size(o, o)
            )
            translate(centerX, centerY) {
                drawPaths(path, d, 1f, aColor.getColor())
            }
        } else {
            drawRect(
                color = aColor.getColor(),
                topLeft = Offset(
                    0f,
                    h / 2 - w / 2
                ),
                size = Size(w, w)
            )

            val tt = map(t, 0.5f, 1f, 0f, 1f)
            drawIntoCanvas { canvas ->
                canvas.saveLayer(size.toRect(), paint)
                translate(centerX, centerY) {
                    drawPaths(path, d, tt, aColor.invert().getColor(), true)
                }
                canvas.restore()
            }

            isDone = true
        }
    }

}

val matrix = arrayOf(-1 to -1, 1 to -1, -1 to 1, 1 to 1)

fun DrawScope.drawPaths(
    path: Path,
    d: Float,
    t: Float,
    color: Color,
    shouldBlend: Boolean = false,
) {
    for (i in 0 until 4) {
        rotate(
            180 * easing2(t), pivot = Offset(
                (matrix[i].first * d / 2),
                (matrix[i].second * d / 2)
            )
        ) {
            path.apply {
                path.reset()
                moveTo(d * matrix[i].first, 0f)
                lineTo(d * matrix[i].first, d * matrix[i].second)
                lineTo(0f, d * matrix[i].second)
                close()
            }
            val mode = if (shouldBlend) BlendMode.Xor else DefaultBlendMode
            drawPath(path = path, color = color, style = Fill, blendMode = mode)
        }
    }

}


fun progress(p: Float, start: Float, end: Float) = start + (end - start) * p

enum class AColor {
    WHITE,
    MAGENTA;

    fun invert(): AColor = if (this == WHITE) MAGENTA else WHITE

    fun getColor(): Color = if (this == WHITE) Color.White else Color(234, 73, 95)
}

fun easing1(x: Float): Float {
    return when {
        x == 0f -> {
            0f
        }

        x == 1f -> {
            1f
        }

        x < 0.5f -> {
            2.0.pow(20 * x - 10.0).toFloat() / 2
        }

        else -> {
            (2f - 2.0.pow(-20f * x + 10.0)).toFloat() / 2
        }
    }

}

fun easing2(x: Float): Float =
    if (x < 0.5) (8 * x * x * x * x) else (1 - (-2 * x + 2).toDouble().pow(4.0) / 2).toFloat()

fun map(value: Float, start1: Float, stop1: Float, start2: Float, stop2: Float): Float {
    return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1))
}