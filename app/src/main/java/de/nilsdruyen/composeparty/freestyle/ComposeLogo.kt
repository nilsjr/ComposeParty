package de.nilsdruyen.composeparty.freestyle

import android.graphics.Matrix
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun ComposeLogo() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val infiniteTransition = rememberInfiniteTransition()
            val logoSizeModifier = Modifier.size(width = 143.dp, height = 160.dp)

            Text("figma vector")
            Spacer(Modifier.height(16.dp))
            Box {
                ComposeFigma(
                    modifier = logoSizeModifier,
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 8f,
                    dashOffset = floatArrayOf(40f, 20f),
                    phase = animatedPhase(
                        transition = infiniteTransition,
                        duration = 2000,
                        phase = 60f
                    )
                )
                ComposeFigma(
                    modifier = logoSizeModifier.scale(0.5f),
                    color = MaterialTheme.colorScheme.tertiary,
                    strokeWidth = 72f,
                    dashOffset = floatArrayOf(200f, 100f),
                    phase = animatedPhase(
                        transition = infiniteTransition,
                        duration = 10000,
                        phase = 300f
                    ),
                )
            }
            Spacer(Modifier.height(32.dp))
            Text("exported svg")
            Spacer(Modifier.height(16.dp))
            Box {
                ComposeLogo(
                    modifier = logoSizeModifier,
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 8f,
                    dashOffset = floatArrayOf(40f, 20f),
                    phase = animatedPhase(
                        transition = infiniteTransition,
                        duration = 2000,
                        phase = 60f
                    )
                )
                ComposeLogo(
                    modifier = logoSizeModifier.scale(0.5f),
                    color = MaterialTheme.colorScheme.tertiary,
                    strokeWidth = 72f,
                    dashOffset = floatArrayOf(200f, 100f),
                    phase = animatedPhase(
                        transition = infiniteTransition,
                        duration = 10000,
                        phase = 300f
                    ),
                )
            }
        }
    }
}

@Composable
private fun ComposeFigma(
    modifier: Modifier,
    color: Color,
    strokeWidth: Float,
    dashOffset: FloatArray,
    phase: Float
) {
    Canvas(
        modifier
    ) {
        /*
         * Path drawn with raw Figma shapes
         */

        val baseWidth = 86.6025390625f
        val baseHeight = 96.90599060058594f

        val path = Path()
        path.fillType = PathFillType.EvenOdd

        path.moveTo(39.9711f, 5.185f)
        path.lineTo(7f, 24.2209f)
        path.cubicTo(5.1436f, 25.2927f, 4f, 27.2735f, 4f, 29.4171f)
        path.lineTo(4f, 67.4889f)
        path.cubicTo(4f, 69.6325f, 5.1436f, 71.6132f, 7f, 72.685f)
        path.lineTo(39.9711f, 91.7209f)
        path.cubicTo(41.8275f, 92.7927f, 44.1147f, 92.7927f, 45.9711f, 91.7209f)
        path.lineTo(78.9423f, 72.685f)
        path.cubicTo(80.7987f, 71.6132f, 81.9423f, 69.6325f, 81.9423f, 67.4889f)
        path.lineTo(81.9423f, 29.4171f)
        path.cubicTo(81.9423f, 27.2735f, 80.7987f, 25.2927f, 78.9423f, 24.2209f)
        path.lineTo(45.9711f, 5.185f)
        path.cubicTo(44.1147f, 4.1132f, 41.8275f, 4.1132f, 39.9711f, 5.185f)
        path.close()

        drawPath(
            path = path
                .asAndroidPath()
                .apply {
                    transform(Matrix().apply {
                        setScale(size.width / baseWidth, size.height / baseHeight)
                    })
                }
                .asComposePath(),
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
                pathEffect = PathEffect.dashPathEffect(dashOffset, phase)
            )
        )
    }
}

@Composable
private fun ComposeLogo(
    modifier: Modifier,
    color: Color,
    strokeWidth: Float,
    dashOffset: FloatArray,
    phase: Float
) {
    Canvas(
        modifier
    ) {
        /*
         * Path drawn with Compose logo
         */

        val baseWidth = 143.2519989013672f
        val baseHeight = 160.31832885742188f
        val path = Path()

        path.moveTo(72.044f, 160.3178f)
        path.cubicTo(69.144f, 160.3418f, 66.29f, 159.6038f, 63.765f, 158.1778f)
        path.lineTo(5.578f, 125.3118f)
        path.cubicTo(2.162f, 123.3888f, 0.041f, 119.7848f, 0.003f, 115.8688f)
        path.lineTo(0.001f, 49.0388f)
        path.cubicTo(-0.027f, 46.1818f, 0.538f, 43.4288f, 1.698f, 41.0038f)
        path.cubicTo(2.945f, 38.3968f, 4.88f, 36.1718f, 7.515f, 34.6148f)
        path.lineTo(62.292f, 2.3008f)
        path.cubicTo(64.796f, 0.8218f, 67.645f, 0.0288f, 70.552f, 0.0008f)
        path.cubicTo(73.461f, -0.0272f, 76.324f, 0.7108f, 78.856f, 2.1408f)
        path.lineTo(134.233f, 33.4188f)
        path.cubicTo(136.702f, 34.8148f, 138.864f, 36.7858f, 140.446f, 39.1188f)
        path.cubicTo(142.171f, 41.6508f, 143.222f, 44.6018f, 143.252f, 47.6828f)
        path.lineTo(143.251f, 111.2808f)
        path.cubicTo(143.278f, 114.1888f, 142.54f, 117.0518f, 141.11f, 119.5838f)
        path.cubicTo(139.681f, 122.1158f, 137.61f, 124.2268f, 135.106f, 125.7048f)
        path.lineTo(80.328f, 158.0238f)
        path.cubicTo(77.772f, 159.5348f, 74.909f, 160.2938f, 72.043f, 160.3178f)
        path.lineTo(72.044f, 160.3178f)
        path.close()

        drawPath(
            path = path
                .asAndroidPath()
                .apply {
                    transform(Matrix().apply {
                        setScale(size.width / baseWidth, size.height / baseHeight)
                    })
                }
                .asComposePath(),
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
                pathEffect = PathEffect.dashPathEffect(dashOffset, phase)
            )
        )
    }
}

@Composable
private fun animatedPhase(
    transition: InfiniteTransition,
    duration: Int = 1000,
    phase: Float
): Float {
    val animatedPhase by transition.animateFloat(
        initialValue = phase,
        targetValue = -phase,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = LinearEasing),
        )
    )
    return animatedPhase
}