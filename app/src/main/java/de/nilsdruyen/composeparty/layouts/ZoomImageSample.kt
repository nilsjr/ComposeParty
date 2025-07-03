package de.nilsdruyen.composeparty.layouts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
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