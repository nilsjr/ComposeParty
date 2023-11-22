package de.nilsdruyen.composeparty.animations

import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.skydoves.orbital.Orbital
import com.skydoves.orbital.animateSharedElementTransition
import com.skydoves.orbital.rememberContentWithOrbitalScope

private const val image = "https://picsum.photos/id/31/1036/2021"

@Composable
fun MoveableSample() {
    OrbitalSharedElementTransitionExample()
}

@Composable
private fun OrbitalSharedElementTransitionExample() {
    var isTransformed by rememberSaveable { mutableStateOf(false) }
    val poster = rememberContentWithOrbitalScope {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = if (isTransformed) {
                Modifier.fillMaxSize()
            } else {
                Modifier.size(130.dp, 220.dp)
            }.animateSharedElementTransition(
                this, SpringSpec(stiffness = 500f), SpringSpec(stiffness = 500f)
            ),
            contentScale = ContentScale.Crop
        )
    }

    Orbital(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .clickable { isTransformed = !isTransformed }
    ) {
        if (isTransformed) {
            Details(sharedElementContent = { poster() }, pressOnBack = {})
        } else {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                poster()
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(image)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.size(130.dp, 220.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun Details(
    sharedElementContent: @Composable () -> Unit,
    pressOnBack: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            sharedElementContent()
        }
    }
}