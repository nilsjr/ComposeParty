package de.nilsdruyen.composeparty.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlin.math.absoluteValue

@Composable
fun AccordionSample(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize()) {
        AccordionLazyRow(
            Modifier
                .fillMaxWidth()
                .align(Alignment.Center))
    }
}

@Composable
fun AccordionLazyRow(modifier: Modifier = Modifier) {
    var width by remember { mutableFloatStateOf(1f) }
    LazyRow(
        modifier = modifier.onSizeChanged {
            width = it.width.toFloat()
        },
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = 80.dp)
    ) {
        items(100) { page ->
            var x by remember { mutableFloatStateOf(0f) }
            val zIndex by remember {
                derivedStateOf {
                    (width - (x - (width / 2)).absoluteValue).toInt().toFloat()
                }
            }
            Box(
                modifier = Modifier
                    .zIndex(zIndex)
                    .requiredWidth(40.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://source.unsplash.com/random/1920x1080/?greece,${page}")
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .onGloballyPositioned { x = it.positionInWindow().x }
                        .graphicsLayer {
                            rotationY = (x / width).lerp(0f, 180f)
                            transformOrigin = TransformOrigin(1f, .5f)
                            cameraDistance = 10f
                        }
                        .height(120.dp)
                        .requiredWidth(300.dp)
                        .shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(20.dp),
                            spotColor = Color.Black.copy(alpha = .2f),
                        )
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFFFF9E6))
                )
            }
        }
    }
}


fun Float.lerp(start: Float, end: Float) = start + this * (end - start)
