package de.nilsdruyen.composeparty.animations

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import de.nilsdruyen.composeparty.R
import de.nilsdruyen.composeparty.utils.Centered

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun AnimatedVectorDrawable() {
    val image = AnimatedImageVector.animatedVectorResource(R.drawable.ic_loading)
    val atEnd by remember { mutableStateOf(false) }

    Centered(Modifier.background(Color.Black)) {
        Icon(
            painter = rememberAnimatedVectorPainter(image, atEnd),
            contentDescription = null // decorative element
        )
    }
}