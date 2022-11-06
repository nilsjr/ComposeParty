package de.nilsdruyen.composeparty.cards

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import de.nilsdruyen.composeparty.utils.Center

private val NilsInfo = PeopleInfo(
    name = "Nils Druyen",
    imageUrl = "https://data.nilsdruyen.de/nils-transparent.png",
    position = "",
    logoUrl = "",
    favoriteColor = Color.Blue,
)

@OptIn(ExperimentalTextApi::class)
@Preview
@Composable
fun PeopleCardSample() {
    val measurer = rememberTextMeasurer()

    Center {
        Card(
            modifier = Modifier
                .fillMaxWidth(fraction = .9f)
                .aspectRatio(.8f)
                .align(Alignment.Center),
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 12.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(12.dp, NilsInfo.favoriteColor, shape = RoundedCornerShape(32.dp))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(NilsInfo.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(1.1f)
                        .aspectRatio(.9f)
                        .align(Alignment.BottomCenter),
                    contentScale = ContentScale.Crop,
                )
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val text = measurer.measure(
                        AnnotatedString(NilsInfo.name),
                        style = TextStyle(
                            fontSize = 36.sp,
                            color = Color.Black,
                        ),
                    )
                    val startPadding = 16.dp
                    drawText(text, topLeft = Offset(startPadding.toPx(), size.height * .7f))
                }
            }
        }
    }
}

internal data class PeopleInfo(
    val name: String,
    val imageUrl: String,
    val position: String,
    val logoUrl: String,
    val favoriteColor: Color,
)