package de.nilsdruyen.composeparty.layouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import de.nilsdruyen.composeparty.R
import de.nilsdruyen.composeparty.data.images
import timber.log.Timber
import kotlin.math.roundToInt

@Composable
fun HorizontalParallaxList() {
    var offset by remember { mutableFloatStateOf(0f) }
    val lazyListState = rememberLazyListState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(images.first())
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .scale(scaleX = 1.4f, 1f),
                alignment = BiasAlignment(offset, 0f),
            )
        }
        Image(
            painter = painterResource(id = R.drawable.bg_forest),
            contentDescription = null,
//            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(400.dp)
                .height(200.dp),
            alignment = BiasAlignment(offset, 0f),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Slider(
            value = offset,
            onValueChange = {
                Timber.d("change $it")
                offset = it
            },
            valueRange = -1f..1f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(images, key = { it }) { url ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(url)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        alignment = ParallaxAlignment(
                            horizontalBias = {
                                // Read the LazyListState layout info
                                val layoutInfo = lazyListState.layoutInfo
                                // Find the layout info of this item
                                val itemInfo = layoutInfo.visibleItemsInfo.first { it.key == url }

                                val adjustedOffset =
                                    itemInfo.offset - layoutInfo.viewportStartOffset
                                (adjustedOffset / itemInfo.size.toFloat()).coerceIn(-1f, 1f).also {
                                    Timber.d("item: ${itemInfo.index} $it $adjustedOffset ${itemInfo.size}")
                                }
                            }
                        )
                    )
                    Text(
                        text = "Hallo Nils",
                        modifier = Modifier
                            .align(
                                ParallaxAlignment(
                                    horizontalBias = {
                                        // Read the LazyListState layout info
                                        val layoutInfo = lazyListState.layoutInfo
                                        // Find the layout info of this item
                                        val itemInfo =
                                            layoutInfo.visibleItemsInfo.first { it.key == url }

                                        val adjustedOffset =
                                            itemInfo.offset - layoutInfo.viewportStartOffset
                                        (adjustedOffset / itemInfo.size.toFloat()).coerceIn(-1f, 1f)
                                    },
                                    verticalBias = { 1f },
                                )
                            )
                            .padding(32.dp),
                        color = Color.White,
                        fontSize = 24.sp,
                    )
                }
            }
        }
    }
}

@Stable
class ParallaxAlignment(
    private val horizontalBias: () -> Float = { 0f },
    private val verticalBias: () -> Float = { 0f },
) : Alignment {
    override fun align(
        size: IntSize,
        space: IntSize,
        layoutDirection: LayoutDirection,
    ): IntOffset {
        // Convert to Px first and only round at the end, to avoid rounding twice while calculating
        // the new positions
        val centerX = (space.width - size.width).toFloat() / 2f
        val centerY = (space.height - size.height).toFloat() / 2f
        val resolvedHorizontalBias = if (layoutDirection == LayoutDirection.Ltr) {
            horizontalBias()
        } else {
            -1 * horizontalBias()
        }

        val x = centerX * (1 + resolvedHorizontalBias)
        val y = centerY * (1 + verticalBias())
        return IntOffset(x.roundToInt(), y.roundToInt())
    }
}