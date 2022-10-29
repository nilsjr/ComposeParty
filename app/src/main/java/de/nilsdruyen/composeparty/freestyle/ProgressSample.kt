package de.nilsdruyen.composeparty.freestyle

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
                .drawWithCache {
                    val text = measurer.measure(AnnotatedString("${percentage.value}%"))
                    val offset = Offset(
                        x = size.width / 2 - text.size.width / 2,
                        y = size.height / 2 - text.size.height / 2
                    )
                    onDrawBehind {
                        drawText(text, topLeft = offset)
                    }
                }
        ) {
            drawArc(
                brush = SolidColor(Color.LightGray),
                startAngle = 120f,
                sweepAngle = 300f,
                useCenter = false,
                style = Stroke(35f, cap = StrokeCap.Round)
            )

            val convertedValue = progress * 300
            drawArc(
                brush = SolidColor(Color.Cyan),
                startAngle = 120f,
                sweepAngle = convertedValue,
                useCenter = false,
                style = Stroke(35f, cap = StrokeCap.Round)
            )
        }

//        Text(text = "$percentage%", modifier = Modifier.align(Alignment.Center))

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