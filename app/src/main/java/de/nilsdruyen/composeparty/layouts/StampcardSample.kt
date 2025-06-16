package de.nilsdruyen.composeparty.layouts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.fressnapf.app.design.modifiers.dashedBorder
import com.google.common.collect.Multimaps.index
import de.nilsdruyen.composeparty.R
import de.nilsdruyen.composeparty.freestyle.Presets
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Spread
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

@Composable
fun StampcardSample(modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier) {
        Column(Modifier.padding(it)) {
            StampCard()

            val pagerState = rememberPagerState { 2 }
            HorizontalPager(
                state = pagerState,
                pageSize = PageSize.Fill
            ) {
                StampCard()
            }
        }
    }
}

private data class Stamp(
    val earned: Boolean,
    val isReward: Boolean = false,
    val number: Int,
)

@Composable
fun StampCard(modifier: Modifier = Modifier) {
    val rewardOffset = remember { mutableStateOf(Offset.Unspecified) }
    val party = remember { mutableStateOf<Party?>(null) }

    LaunchedEffect(rewardOffset) {
        if (rewardOffset.value != Offset.Unspecified) {
            party.value = Party(
                speed = 0f,
                maxSpeed = 10f,
                damping = 0.9f,
                angle = Angle.TOP,
                spread = Spread.ROUND,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                emitter = Emitter(duration = 20, TimeUnit.SECONDS).perSecond(30),
                position = Position.Absolute(rewardOffset.value.x, rewardOffset.value.y)
            )
        }
    }

    Card(
        modifier
            .fillMaxWidth()
            .padding(PaddingValues(16.dp))
    ) {
        Box {
            val partieee = party.value
            Column {
                Spacer(Modifier.size(16.dp))
                Row(
                    Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Friends Pawty collector",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Black)) {
                                append("5")
                            }
                            append(" left")
                        },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(Modifier.size(16.dp))
                StampList(
                    rewardOffset = { offset -> rewardOffset.value = offset },
                    party = party,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.size(16.dp))
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                    )
                ) {
                    Text(
                        "\uD83E\uDD73 Yippeeh! You earned a reward will be available from tomorrow, 08:00 o’clock.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(Modifier.size(16.dp))
            }
            if (partieee != null) {
                KonfettiView(
                    modifier = Modifier.matchParentSize(),
                    parties = listOf(partieee),
                )
            }
        }
    }
}

@Composable
private fun StampList(
    rewardOffset: (Offset) -> Unit,
    party: State<Party?>,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    val stamps = listOf(
        Stamp(earned = true, isReward = false, number = 1),
        Stamp(earned = true, isReward = false, number = 1),
        Stamp(earned = true, isReward = false, number = 1),
        Stamp(earned = true, isReward = true, number = 1),
        Stamp(earned = false, isReward = false, number = 1),
        Stamp(earned = false, isReward = false, number = 1),
        Stamp(earned = false, isReward = false, number = 1),
        Stamp(earned = false, isReward = false, number = 1),
        Stamp(earned = false, isReward = true, number = 1),
    )

    val lastEarned = stamps
        .indexOfLast { it.earned }
        .takeIf { it > 1 }
        .run { this?.minus(1) } ?: 0

    LaunchedEffect(Unit) {
        if (lastEarned > 0) {
            lazyListState.scrollToItem(lastEarned)
        }
    }
    val rewardModifier = Modifier.onGloballyPositioned {
        rewardOffset(it.positionInRoot())
    }

    Box(modifier = modifier) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(20.dp)
                .align(Alignment.Center)
                .background(MaterialTheme.colorScheme.secondaryContainer)
        )
        LazyRow(
            state = lazyListState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            items(stamps) {
                StampItem(
                    stamp = it,
                    modifier = if (it.isReward) rewardModifier else Modifier
                )
            }
        }
    }
}

@Composable
private fun StampItem(stamp: Stamp, modifier: Modifier = Modifier) {
    val backgroundColor = if (stamp.earned) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.secondary
    }
    Box(
        modifier
            .size(48.dp)
            .clip(CircleShape)
            .dashedBorder(2.dp, color = Color.White, shape = CircleShape)
            .background(backgroundColor)
            .padding(4.dp),
        contentAlignment = Alignment.Center,
    ) {
        val resourceId = when {
            stamp.isReward -> R.drawable.ic_reward
            stamp.earned -> R.drawable.ic_check
            else -> -1
        }
        if (resourceId == -1) {
            Text(
                text = stamp.number.toString(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
        } else {
            Icon(
                painter = painterResource(resourceId),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Composable
fun CustomStampcardLayout(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val density = LocalDensity.current
    Layout(
        modifier = modifier,
        content = content,
    ) { measurables, constraints ->
        val maxItemsPerRow = minOf(measurables.size - 1, 6)
        val minSpaceBetweenItems = with(density) {
            (measurables.size - 1) * 12.dp.toPx()
        }

        val placeables = measurables.map { measurable ->
            measurable.measure(constraints.copy(minWidth = 0, minHeight = 0))
        }

        val placeablesSize = placeables.sumOf { it.measuredWidth } + minSpaceBetweenItems
        val stackedLayout =
            placeablesSize > constraints.maxWidth && placeablesSize * 0.6 < constraints.maxWidth

        if (stackedLayout) {
            val firstSpace = placeables.first().width
            val maxSpace = constraints.maxWidth - firstSpace * placeables.size
            val off = maxSpace / (placeables.size - 1) + firstSpace
            var yPos = 0
            var maxHeight = 0

            val items = placeables
                .mapIndexed { index, placeable -> index to placeable }
                .associateBy { (index, placeable) ->
                    val x = when (index) {
                        0 -> 0
                        placeables.indices.last -> constraints.maxWidth - placeable.width
                        else -> off * index
                    }
                    IntOffset(x, yPos).also {
                        yPos = if (yPos == 0) {
                            (placeable.height * 0.8).roundToInt()
                        } else {
                            0
                        }
                        maxHeight = maxOf(maxHeight, placeable.height + yPos)
                    }
                }
                .mapValues { it.value.second }

            layout(constraints.maxWidth, maxHeight) {
                items.forEach { offset, placeable ->
                    placeable.placeRelative(x = offset.x, y = offset.y)
                }
            }
        } else {
            val width = placeables.first().width
            val maxSpace = constraints.maxWidth - width * maxItemsPerRow
            val offset = maxSpace / (maxItemsPerRow - 1) + width
            if (maxItemsPerRow < 5) {
                val height = placeables.maxOf { it.height }

                layout(constraints.maxWidth, height) {
                    placeables.forEachIndexed { index, placeable ->
                        placeable.placeRelative(x = offset * index, y = 0)
                    }
                }
            } else {
                val rowCount = (placeables.size + maxItemsPerRow - 1) / maxItemsPerRow
                val height = placeables.maxOf { it.height }
                var heighOffset = 0
                val layoutHeight = rowCount * height + (rowCount - 1) * 12.dp.roundToPx()

                val windowedPlaceables = placeables
                    .windowed(size = maxItemsPerRow, step = maxItemsPerRow, partialWindows = true)

                layout(constraints.maxWidth, layoutHeight) {
                    windowedPlaceables.forEachIndexed { yIndex, placeableRow ->
                        placeableRow.forEachIndexed { xIndex, placeable ->
                            placeable.placeRelative(x = offset * xIndex, y = heighOffset)
                        }
                        heighOffset += height + 12.dp.roundToPx()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun StampcardSamplePreview() {
    ComposePartyTheme {
        StampcardSample()
    }
}

@Preview
@Composable
private fun StampcardCustomSamplePreview() {
    val modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
    ComposePartyTheme {
        Column {
            CustomStampcardLayout(modifier = modifier) {
                buildStamps(4, 1).forEach {
                    StampItem(stamp = it)
                }
            }
            CustomStampcardLayout(modifier = modifier) {
                buildStamps(5, 1).forEach {
                    StampItem(stamp = it)
                }
            }
            CustomStampcardLayout(modifier = modifier) {
                buildStamps(7, 1).forEach {
                    StampItem(stamp = it)
                }
            }
            CustomStampcardLayout(modifier = modifier) {
                buildStamps(10, 1).forEach {
                    StampItem(stamp = it)
                }
            }
            CustomStampcardLayout(modifier = modifier) {
                buildStamps(13, 1).forEach {
                    StampItem(stamp = it)
                }
            }
            CustomStampcardLayout(modifier = modifier) {
                buildStamps(17, 1).forEach {
                    StampItem(stamp = it)
                }
            }
        }
    }
}

private fun buildStamps(size: Int, earned: Int): List<Stamp> {
    return buildList {
        repeat(size - 1) { i ->
            add(Stamp(earned = i < earned, number = i + 1))
        }
        add(Stamp(earned = true, isReward = true, number = size))
    }
}