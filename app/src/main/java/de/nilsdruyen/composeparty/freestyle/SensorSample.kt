package de.nilsdruyen.composeparty.freestyle

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.nilsdruyen.composeparty.R
import de.nilsdruyen.composeparty.utils.SensorManager
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun SensorSample() {
    Box(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(
            Modifier
                .fillMaxSize(fraction = .75f)
                .background(Color.Red)
                .align(Alignment.Center)
        ) {
            val maxOffsetX = (minWidth * 1.2f - minWidth) / 2
            val maxOffsetY = (minHeight * 1.2f - minHeight) / 2
            val offset = rememberSensorOffset(maxOffsetX, maxOffsetY, 3f)

            Image(painter = painterResource(id = R.drawable.bg_forest),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(.5f)
                    .offset { offset.value }
                    .scale(1.2f)
                    .withOverlay(
                        brush = Brush.verticalGradient(
                            0.00f to Color(0x00000000),
                            0.27f to Color(0x0A000000),
                            0.56f to Color(0x7A000000),
                            1.00f to Color(0xE5000000),
                        )
                    ))
            Text(
                text = "FRESSNAPF",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(32.dp),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
        }
    }
}

@Composable
fun rememberSensorOffset(
    maxOffsetX: Dp,
    maxOffsetY: Dp,
    multiplier: Float = 2f
): Animatable<IntOffset, AnimationVector2D> {
    Timber.d("$maxOffsetX - $maxOffsetY")

    val context = LocalContext.current
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val offset = remember { Animatable(IntOffset.Zero, IntOffset.VectorConverter) }

    val xIntRange = with(density) { maxOffsetX.roundToPx()..maxOffsetX.roundToPx() }
    val yIntRange = with(density) { maxOffsetY.roundToPx()..maxOffsetY.roundToPx() }

    DisposableEffect(Unit) {
        val sensorManager = SensorManager(context)
        sensorManager.start()
        sensorManager.onChangeListener = {
            scope.launch {
                with(density) {
                    offset.animateTo(
                        targetValue = it.roundToPx(multiplier, xIntRange, yIntRange),
                        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
                    )
                }
            }
        }
        onDispose {
            sensorManager.stop()
        }
    }
    return offset
}

context(Density)
fun Offset.roundToPx(multiplier: Float, rangeX: IntRange, rangeY: IntRange): IntOffset {
    return IntOffset(
        x = ((x * multiplier).dp.roundToPx()).coerceIn(rangeX),
        y = ((-y * multiplier).dp.roundToPx()).coerceIn(rangeY),
    )
}

fun Modifier.withOverlay(brush: Brush, blendMode: BlendMode = BlendMode.Darken) = drawWithCache {
    onDrawWithContent {
        drawContent()
        drawRect(brush, blendMode = blendMode)
    }
}

@Preview
@Composable
fun SensorPreview() {
    SensorSample()
}