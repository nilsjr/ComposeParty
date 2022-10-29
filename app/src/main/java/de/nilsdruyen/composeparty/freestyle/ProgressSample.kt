package de.nilsdruyen.composeparty.freestyle

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalTextApi::class)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ProgressSample() {
    var progress by remember { mutableStateOf(.1f) }
    val measurer = rememberTextMeasurer()
    val percentage = remember {
        derivedStateOf {
            (progress * 100).toInt()
        }
    }

    val gradientColors = listOf(
        Color.Blue,
        Color.DarkGray
    )
    val brush = Brush.linearGradient(gradientColors)
    val convertedValue = progress * 300

    var angle by remember { mutableStateOf(0f) }
    var dragStartedAngle by remember { mutableStateOf(0f) }
    var oldAngle by remember { mutableStateOf(angle) }
    var volume by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        Canvas(
            modifier = Modifier
                .size(250.dp)
                .padding(16.dp)
                .align(Alignment.Center)
                .pointerInput(true) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            dragStartedAngle = atan2(
                                y = size.center.x - offset.x,
                                x = size.center.y - offset.y
                            ) * (180f / Math.PI.toFloat()) * -1
                        },
                        onDragEnd = {
                            oldAngle = angle
                        }
                    ) { change, _ ->
//                        val touchAngle = atan2(
//                            y = size.center.x - change.position.x,
//                            x = size.center.y - change.position.y
//                        ) * (180f / Math.PI.toFloat()) * -1
//
//                        angle = oldAngle + (touchAngle - dragStartedAngle)
//
//                        //we want to work with positive angles
//                        if (angle > 360) {
//                            angle -= 360
//                        } else if (angle < 0) {
//                            angle = 360 - abs(angle)
//                        }
//
//                        if (angle > 360f - (offsetAngleDegree * .8f))
//                            angle = 0f
//                        else if (angle > 0f && angle < offsetAngleDegree)
//                            angle = offsetAngleDegree
//                        //determinants the state of the nob. OFF or ON
//                        state =
//                            angle >= offsetAngleDegree && angle <= (360f - offsetAngleDegree / 2)
//
//                        val newVolume = if (angle < offsetAngleDegree)
//                            0f
//                        else
//                            (angle) / (360f - offsetAngleDegree)
//
//                        volume = newVolume.coerceIn(
//                            minimumValue = 0f,
//                            maximumValue = 1f
//                        )
//
//                        onVolumeChanged(newVolume)
                    }
                }
        ) {
            drawCircle(
                brush = brush,
                alpha = .7f,
            )
            drawArc(
                brush = SolidColor(Color.LightGray),
                startAngle = 120f,
                sweepAngle = 300f,
                useCenter = false,
                style = Stroke(35f, cap = StrokeCap.Round)
            )
            drawArc(
                brush = brush,
                startAngle = 120f,
                sweepAngle = convertedValue,
                useCenter = false,
                style = Stroke(35f, cap = StrokeCap.Round)
            )
            val text = measurer.measure(
                AnnotatedString("${percentage.value}%"),
                style = TextStyle(
                    fontSize = 45.sp,
                    color = Color.White,
                )
            )
            val offset = Offset(
                x = size.width / 2 - text.size.width / 2,
                y = size.height / 2 - text.size.height / 2
            )
            drawText(text, topLeft = offset)

            val radius = 50f
            val buttonCenter = Offset(
                x = (size.width / 2) * cos((convertedValue + 120f).toRad()) + size.center.x,
                y = (size.height / 2) * sin((convertedValue + 120f).toRad()) + size.center.y,
            )
            drawCircle(color = Color.Red, center = buttonCenter, radius = radius)
        }
        Slider(
            value = progress,
            onValueChange = { progress = it },
            valueRange = 0f..1f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

private fun Float.toRad(): Float {
    return this * (Math.PI / 180f).toFloat()
}