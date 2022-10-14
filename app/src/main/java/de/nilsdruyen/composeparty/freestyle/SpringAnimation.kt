package de.nilsdruyen.composeparty.freestyle

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import kotlinx.coroutines.launch

@Composable
fun SpringAnimation() {
    val scope = rememberCoroutineScope()
    val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
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

//        Spacer(modifier = Modifier.height(30.dp))
//        Circle(modifier = Modifier
//            .offset { offset2.value.toIntOffset() }
//            .background(Color.White.copy(0.8f), CircleShape))
//
//
//        Spacer(modifier = Modifier.height(30.dp))
//        Circle(modifier = Modifier
//            .offset { offset3.value.toIntOffset() }
//            .background(Color.White.copy(0.3f), CircleShape))
    }
}