package de.nilsdruyen.composeparty.freestyle

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import de.nilsdruyen.composeparty.freestyle.explodable.Explodable
import de.nilsdruyen.composeparty.freestyle.explodable.ExplosionAnimationSpec
import de.nilsdruyen.composeparty.freestyle.explodable.rememberExplosionController
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@Composable
fun RevealImageSample() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(Color.Black),
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(model = imageUrl, contentDescription = null)
        Wall(Modifier.fillMaxSize())
    }
}

@Composable
fun Wall(modifier: Modifier) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val grid by remember {
        val cellsRatio = if (screenWidth > screenHeight) 3 to 7 else 7 to 3
        mutableStateOf(cellsRatio)
    }
    val count by remember { derivedStateOf { grid.first * grid.second } }
    var exploded by remember { mutableIntStateOf(0) }
    val alpha by animateFloatAsState(
        targetValue = if (exploded == count) 0f else 1f,
        animationSpec = tween(2000),
    )

    LaunchedEffect(exploded) {
        if (exploded > count / 2) exploded = count
    }

    Column(modifier.alpha(alpha)) {
        repeat(grid.first) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                repeat(grid.second) {
                    val eC = rememberExplosionController()
                    Explodable(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        controller = eC,
                        onExplode = { exploded++ },
                        animationSpec = ExplosionAnimationSpec(
                            shakeDurationMs = 1000,
                            explosionPower = 5f,
                            explosionDurationMs = 1000,
                        ),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Green)
                                .clickable {
                                    eC.explode()
                                }
                        ) {}
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun WallPreview() {
    ComposePartyTheme {
        Wall(modifier = Modifier.fillMaxSize())
    }
}