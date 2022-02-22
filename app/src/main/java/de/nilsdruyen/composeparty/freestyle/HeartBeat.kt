package de.nilsdruyen.composeparty.freestyle

import android.graphics.Matrix
import android.graphics.Path
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.core.graphics.component3
import androidx.core.graphics.component4

@Preview
@Composable
fun HeartRate() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val transition = rememberInfiniteTransition()
            val infiniteSpec = infiniteRepeatable<Float>(
                animation = tween(6400, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )

            val phaseRotate by transition.animateFloat(
                initialValue = -400f,
                targetValue = 400f,
                animationSpec = infiniteRepeatable(
                    animation = tween(3200, easing = LinearEasing)
                )
            )
            val blueDegrees by transition.animateFloat(
                initialValue = -180f,
                targetValue = 180f,
                animationSpec = infiniteSpec
            )
            val redDegrees by transition.animateFloat(
                initialValue = -60f,
                targetValue = 360f,
                animationSpec = infiniteSpec
            )

            HeartIcon(
                modifier = Modifier.size(48.dp, 44.dp),
                phase = phaseRotate,
                colors = listOf(Color.Blue.hueRotate(blueDegrees), Color.Red.hueRotate(redDegrees)),
                dashArray = floatArrayOf(340f, 60f)
            )
        }
    }
}

@Composable
private fun HeartIcon(
    modifier: Modifier,
    phase: Float,
    colors: List<Color>,
    dashArray: FloatArray
) {
    Canvas(modifier) {
        val baseWidth = 20.0077f
        val baseHeight = 17.6469f

        val path = Path()

        path.moveTo(17.6641f, 0.9907f)
        path.cubicTo(15.0241f, -0.8093f, 11.7641f, 0.0307f, 10.0041f, 2.0907f)
        path.cubicTo(8.2441f, 0.0307f, 4.9841f, -0.8193f, 2.3441f, 0.9907f)
        path.cubicTo(0.9441f, 1.9507f, 0.0641f, 3.5707f, 0.0041f, 5.2807f)
        path.cubicTo(-0.1359f, 9.1607f, 3.3041f, 12.2707f, 8.5541f, 17.0407f)
        path.lineTo(8.6541f, 17.1307f)
        path.cubicTo(9.4141f, 17.8207f, 10.5841f, 17.8207f, 11.3441f, 17.1207f)
        path.lineTo(11.4541f, 17.0207f)
        path.cubicTo(16.7041f, 12.2607f, 20.1341f, 9.1507f, 20.0041f, 5.2707f)
        path.cubicTo(19.9441f, 3.5707f, 19.0641f, 1.9507f, 17.6641f, 0.9907f)
        path.lineTo(17.6641f, 0.9907f)
        path.close()

        drawPath(
            path = path
                .apply {
                    transform(Matrix().apply {
                        setScale(size.width / baseWidth, size.height / baseHeight)
                    })
                }
                .asComposePath(),
            brush = Brush.linearGradient(
                colors = colors,
                start = Offset(x = 0f, y = size.height / 2),
                end = Offset(x = size.width, y = size.height / 2)
            ),
            style = Stroke(
                width = 16f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
                pathEffect = PathEffect.dashPathEffect(dashArray, phase)
            ))
    }
}

fun Color.hueRotate(degrees: Float): Color {
    val (_, r, g, b) = toArgb()
    val out = FloatArray(3)
    ColorUtils.RGBToHSL(r, g, b, out)
    out[0] = (out[0] + degrees) % 360.0f
    return Color(ColorUtils.HSLToColor(out))
}