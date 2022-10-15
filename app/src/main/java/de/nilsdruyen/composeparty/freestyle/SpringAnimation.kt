package de.nilsdruyen.composeparty.freestyle

import android.view.MotionEvent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import de.nilsdruyen.composeparty.R
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SpringAnimation() {
    val scope = rememberCoroutineScope()
    val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
    val imageOffset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
//    val offset2 = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
//    val offset3 = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .offset { offset.value.round() }
                .size(100.dp)
                .background(Color.Red, CircleShape)
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        change.consume()
                        scope.launch {
                            offset.animateTo(
                                offset.value + change.position, spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy
                                )
                            )
                        }
//                    scope.launch {
//                        offset2.animateTo(
//                            offset.value + change.position, spring(
//                                dampingRatio = Spring.DampingRatioLowBouncy,
//                                stiffness = 100f
//                            )
//                        )
//                    }
//                    scope.launch {
//                        offset3.animateTo(
//                            offset.value + change.position, spring(
//                                dampingRatio = Spring.DampingRatioLowBouncy,
//                                stiffness = StiffnessVeryLow
//                            )
//                        )
//                    }
                    }
                }
        )

        val imageSize = remember { mutableStateOf(Offset.Zero) }
        val imagePosition = remember { mutableStateOf(Offset.Zero) }
        val offsetDiff = remember { mutableStateOf(Offset.Zero) }

        Image(
            painter = painterResource(id = R.drawable.img_fn_logo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .offset { imageOffset.value.round() }
                .size(120.dp)
                .clip(CircleShape)
                .border(1.dp, Color.LightGray, shape = CircleShape)
                .align(Alignment.Center)
                .onGloballyPositioned {
                    val (width, height) = it.size
                    imageSize.value = Offset((width / 2).toFloat(), (height / 2).toFloat())
                    imagePosition.value = it.positionInParent()
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            offsetDiff.value = imagePosition.value - it
                            scope.launch { imageOffset.stop() }
                        },
                        onDragEnd = {
                            scope.launch {
                                Timber.d("stop: ${imageOffset.value}")
                                imageOffset.animateTo(
                                    targetValue = Offset.Zero,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioHighBouncy,
                                        stiffness = Spring.StiffnessMedium,
                                    )
                                )
                            }
                        },
                        onDragCancel = {},
                        onDrag = { change, _ ->
                            change.consume()
                            scope.launch {
                                Timber.d("change: ${imageOffset.value} - ${change.position}")
                                imageOffset.animateTo(
                                    targetValue = imageOffset.value + change.position,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioLowBouncy
                                    )
                                )
                            }
                        },
                    )
                }
        )
    }
}

@Preview
@Composable
fun PreviewSpringAnimation() {
    SpringAnimation()
}