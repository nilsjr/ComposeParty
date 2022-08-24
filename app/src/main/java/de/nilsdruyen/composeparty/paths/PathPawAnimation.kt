package de.nilsdruyen.composeparty.paths

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme
import kotlin.math.PI
import kotlin.math.sin

const val TWO_PI = 2 * PI

@Composable
fun PathPawAnimation() {
    val getY: (Int, Float, Float) -> Float = { x, amplitude, period ->
        (sin(x * TWO_PI / period) * amplitude).toFloat()
    }
    val amplitude: Float = 0f
    val period: Float = 1f

    val start = remember { mutableStateOf(0f) }
    val progress = animateFloatAsState(
        targetValue = start.value,
        animationSpec = tween(durationMillis = 4000, easing = LinearEasing)
    )

    var xState by remember { mutableStateOf(0) }
    val xOffset = animateIntAsState(
        targetValue = xState,
        animationSpec = tween(durationMillis = 4000, easing = LinearEasing)
    )

    LaunchedEffect(Unit) {
        start.value = 1f
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
//                .offset(x = (-80).dp)
//                .rotate(55f)
        ) {
            val stepX = 300
            val x = size.width / 5.0f
            val y = 80.dp.toPx()
            val advance = stepX * 2.0f

            val path = Path()

            path.moveTo(0f, size.height / 2)
            path.relativeQuadraticBezierTo(x, y, advance, 0.0f)
            path.relativeQuadraticBezierTo(x, -y, advance, 0.0f)
            path.relativeQuadraticBezierTo(x, y, advance, 0.0f)
            path.relativeQuadraticBezierTo(x, -y, advance, 0.0f)

            val position = FloatArray(2)
            val tangent = FloatArray(2)

            val measure = android.graphics.PathMeasure(path.asAndroidPath(), false)

            val length = measure.length

            val segment = android.graphics.Path()
            measure.getSegment(0.0f, length * progress.value, segment, true)

            measure.getPosTan(length * progress.value, position, tangent)

//            drawPath(
//                path = path,
//                color = Color.DarkGray,
//                style = Stroke(width = 5f),
//            )

            drawPath(
                path = segment.asComposePath(),
                color = Color.Blue,
                style = Stroke(width = 10f),
            )

            drawCircle(
                Color.Red,
                radius = 8.dp.toPx(),
                center = Offset(position[0], position[1])
            )
        }
//        PawIcon(
//            modifier = Modifier
//                .scale(.5f)
//                .offset(
//                    x = xOffset.value.dp,
//                    y = getY(xOffset.value, 0f, 0f).dp
//                )
//                .rotate(45f)
//                .align(Alignment.Center),
//            color = Color.DarkGray,
//        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xfff)
@Composable
fun PreviewPathPawAnimation() {
    ComposePartyTheme {
        PathPawAnimation()
    }
}