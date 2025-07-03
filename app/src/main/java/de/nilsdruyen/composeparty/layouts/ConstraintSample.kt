package de.nilsdruyen.composeparty.layouts

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@Composable
fun ConstraintSample(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val bias by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1_500),
            repeatMode = RepeatMode.Reverse,
        )
    )

    val constraintSet = ConstraintSet {
        val image = createRefFor("image")
        val text = createRefFor("text")

        constrain(image) {
            width = Dimension.value(40.dp)
            height = Dimension.value(40.dp)
            linkTo(parent.top, parent.bottom, 16.dp, 16.dp)
            start.linkTo(parent.start, 16.dp)
        }
        constrain(text) {
            linkTo(image.top, image.bottom)
            linkTo(image.end, parent.end, bias = bias)
        }
    }
    ConstraintLayout(constraintSet, modifier.background(Color.White)) {
        Box(
            modifier = Modifier
                .layoutId("image")
                .background(Color.Red)
        )
        Text(
            text = "Hallo",
            modifier = Modifier.layoutId("text"),
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}

@Preview
@Composable
private fun ConstraintSamplePreview() {
    ComposePartyTheme {
        ConstraintSample(Modifier.fillMaxWidth())
    }
}