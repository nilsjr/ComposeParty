package de.nilsdruyen.composeparty.paths

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import de.nilsdruyen.composeparty.R
import kotlinx.coroutines.delay

@ExperimentalAnimationGraphicsApi
@Composable
fun PawVDIcon() {
    val image = AnimatedImageVector.animatedVectorResource(R.drawable.paw_loader)
    var atEnd by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            atEnd = !atEnd
            delay(1500)
        }
    }
    Icon(
        painter = rememberAnimatedVectorPainter(image, atEnd),
        contentDescription = null, // decorative element
        tint = Color.White,
    )
}