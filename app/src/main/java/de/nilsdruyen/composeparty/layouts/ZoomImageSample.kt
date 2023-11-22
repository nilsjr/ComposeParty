package de.nilsdruyen.composeparty.layouts

import android.view.MotionEvent
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter.State.Empty.painter
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@Composable
fun ZoomImageSample() {
    val zoomState = rememberZoomState()
    AsyncImage(
        model = "https://picsum.photos/id/31/1036/2021",
        contentDescription = null,
        contentScale = ContentScale.Crop,
        onSuccess = { state ->
            zoomState.setContentSize(state.painter.intrinsicSize)
        },
        modifier = Modifier
            .fillMaxSize()
            .zoomable(zoomState)
    )
}