package de.nilsdruyen.composeparty.design

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.R
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarWarsSample(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("BB-SERIES")
                },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth(.8f)
                        .height(500.dp),
                    shape = starWarsShape,
                ) {
                    Box {
                        Image(
                            painter = painterResource(id = R.drawable.galaxy),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                        Image(
                            painter = painterResource(id = R.drawable.img_bb8),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth(.9f)
                                .graphicsLayer {
                                    translationX = 40f
                                }
                                .align(Alignment.BottomEnd),
                        )
                        Text(
                            text = "BB-8",
                            color = Color.White,
                            style = MaterialTheme.typography.displayLarge.copy(
                                shadow = Shadow(
                                    color = Color.Black,
                                    offset = Offset(10f, 10f),
                                    blurRadius = 20f
                                )
                            ),
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 16.dp, bottom = 32.dp),
                        )
                        Text("")
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp)
                        .rotateVertically(false),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "MODEL:",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.alpha(.6f)
                    )
                    Text(
                        text = "BB",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                    )
                    Spacer(Modifier.size(32.dp))
                    Text(
                        text = "TYPE: ",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.alpha(.6f)
                    )
                    Text(
                        text = "Astromechdroide",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                    )
                }
            }
            Spacer(Modifier.size(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Spacer(Modifier.size(8.dp))
                InfoTile(
                    label = "COLOR",
                    value = "White / Orange",
                )
                InfoTile(
                    label = "Height",
                    value = "200",
                )
                InfoTile(
                    label = "ACCESSORY",
                    value = "Stuff",
                )
            }
            Text(
                text = stringResource(id = R.string.lorem),
                modifier = Modifier.padding(16.dp),
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun InfoTile(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        modifier = modifier.widthIn(64.dp),
        shape = starWarsShapeSmall,
    ) {
        Spacer(Modifier.size(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(Modifier.size(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(start = 8.dp, end = 16.dp)
        )
        Spacer(Modifier.size(8.dp))
    }
}

val starWarsShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 160.dp)
val starWarsShapeSmall =
    RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 8.dp, bottomEnd = 32.dp)

fun Modifier.rotateVertically(clockwise: Boolean = true): Modifier {
    val rotate = rotate(if (clockwise) 90f else -90f)

    val adjustBounds = layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.height, placeable.width) {
            placeable.place(
                x = -(placeable.width / 2 - placeable.height / 2),
                y = -(placeable.height / 2 - placeable.width / 2)
            )
        }
    }
    return rotate then adjustBounds
}

@Preview
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StarWarsSamplePreview() {
    ComposePartyTheme {
        StarWarsSample()
    }
}
