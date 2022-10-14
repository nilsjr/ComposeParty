package de.nilsdruyen.composeparty.freestyle

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import de.nilsdruyen.composeparty.R
import de.nilsdruyen.composeparty.utils.SensorManager
import de.nilsdruyen.composeparty.utils.toOffset
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun SensorSample() {
    val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }

    DisposableSensorEffect(0.3f) {
        offset.animateTo(it, spring(dampingRatio = Spring.DampingRatioLowBouncy))
    }

    Timber.d("offset: ${offset.value}")

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_forest),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .offset {
                    offset.value.round()
                }
                .scale(1.1f)
        )
//        Box(
//            modifier = Modifier
//                .offset {
//                    offset.value
//                        .round()
//                        .times(1.3f)
//                }
//                .size(100.dp)
//                .clip(RoundedCornerShape(8.dp))
//                .background(Color.Red)
//                .align(Alignment.Center)
//        )
    }
}

@Preview
@Composable
fun SensorPreview() {
    SensorSample()
}

@Composable
fun DisposableSensorEffect(muliplier: Float = 1f, onChange: suspend (Offset) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    DisposableEffect(key1 = Unit, effect = {
        val sensorManager = SensorManager(context)
        sensorManager.start()
        sensorManager.onChangeListener = {
            scope.launch {
                onChange(it.toOffset().times(muliplier))
//                offset.animateTo(
//                    it.toOffset(), spring(
//                        dampingRatio = Spring.DampingRatioLowBouncy
//                    )
//                )
            }
        }
        onDispose {
            sensorManager.stop()
        }
    })
}