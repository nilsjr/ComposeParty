package de.nilsdruyen.composeparty.swipe

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.R

const val Background = 0xFF5CD2D2
const val Arrow1 = 0xFF432990
const val Arrow2 = 0xFF4824F7

@Composable
fun SwipeDemo() {
    val viewModel = remember {
        SwipeViewModel()
    }
    val game = viewModel.gameState.collectAsState()
    GameComponent(game = game.value, viewModel::performAction)
}

@Composable
fun GameComponent(game: Game, onUserSwipe: (angle: Angle) -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(game.config.backgroundColor))
            .clickable(onClick = { onUserSwipe(Angle.Right) })
    ) {
        with(game.config) {
            ArrowGrid(
                config = this,
                arrowList = game.buildArrows(),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun ArrowGrid(config: Config, arrowList: List<Arrow>, modifier: Modifier = Modifier) {
    Row(modifier = modifier.wrapContentSize(align = Alignment.Center)) {
        var index = 0
        repeat(config.rowCount) {
            Column {
                repeat(config.columnCount) {
                    val arrow = arrowList[index++]
                    Arrow(arrow.direction, Color(arrow.color))
                }
            }
        }
    }
}

@Composable
fun Arrow(direction: Angle, color: Color) {
    Image(
        painter = painterResource(R.drawable.ic_arr),
        contentDescription = null,
        colorFilter = ColorFilter.tint(color),
        modifier = Modifier
            .size(40.dp)
            .padding(4.dp)
            .graphicsLayer { rotationZ = direction.rotationZ },
    )
}

@Preview(widthDp = 300, heightDp = 250)
@Composable
fun PreviewSmallGrid() {
    val game = Game(
        Config(
            3,
            3,
            Background,
            listOf(Arrow1, Arrow2),
            Angle.Top,
            Angle.Right,
            Position(2, 2),
            0f,
            0
        )
    )

    GameComponent(game = game)
}

@Preview(widthDp = 300, heightDp = 250)
@Composable
fun PreviewGrid() {
    val game = Game(
        Config(
            6,
            6,
            Background,
            listOf(Arrow1, Arrow2),
            Angle.Top,
            Angle.Right,
            Position(2, 2),
            0f,
            0
        )
    )

    GameComponent(game = game)
}