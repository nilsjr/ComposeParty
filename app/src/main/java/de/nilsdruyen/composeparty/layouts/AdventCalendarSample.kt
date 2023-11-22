package de.nilsdruyen.composeparty.layouts

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.R
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

val colors = listOf(
    Color(0xFFF2FFE9),
    Color(0xFFA6CF98),
    Color(0xFF557C55),
    Color(0xFFFA7070),
    Color(0xFFECE3CE),
    Color(0xFF739072),
    Color(0xFF4F6F52),
    Color(0xFF3A4D39),
)

val icons = listOf(
    R.drawable.baubles,
    R.drawable.bell,
    R.drawable.calendar,
    R.drawable.candle,
    R.drawable.candy,
    R.drawable.cane,
    R.drawable.crystal,
    R.drawable.gift,
    R.drawable.gingerbread,
    R.drawable.gloves,
    R.drawable.hat,
    R.drawable.pie,
    R.drawable.reindeer,
    R.drawable.santa,
    R.drawable.snowman,
    R.drawable.star,
    R.drawable.stocking,
    R.drawable.xmas_bauble,
    R.drawable.xmas_bauble_alt,
    R.drawable.xmas_tree,
).shuffled()

sealed interface CalendarScreen {

    data object Overview : CalendarScreen
    data class Detail(val day: Day) : CalendarScreen
}

data class Day(
    val number: Int,
    val color: Color,
    @DrawableRes val icon: Int,
)

val days = List(25) {
    val number = it + 1
    val color = colors.random()
    val icon = if (it in icons.indices) icons[it] else icons.random()
    Day(number, color, icon)
}

@Composable
fun AdventCalendarSample() {
    var screen by remember { mutableStateOf<CalendarScreen>(CalendarScreen.Overview) }

    BackHandler(screen != CalendarScreen.Overview) {
        if (screen != CalendarScreen.Overview) {
            screen = CalendarScreen.Overview
        }
    }

    AnimatedContent(
        targetState = screen,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "screenTransition"
    ) {
        when (it) {
            CalendarScreen.Overview -> Grid {
                screen = CalendarScreen.Detail(it)
            }

            is CalendarScreen.Detail -> Detail(it.day)
        }
    }
}

@Composable
private fun Grid(onClick: (Day) -> Unit) {
    Column(
        Modifier
            .systemBarsPadding()
            .padding(2.dp)
            .background(Color.Black)
    ) {
        repeat(5) { col ->
            val offset = col * 5
            Row(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                repeat(5) { row ->
                    val day = days[(row + offset)]
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .padding(2.dp)
                            .background(day.color)
                            .clickable { onClick(day) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painterResource(id = day.icon),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().padding(4.dp),
                        )
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(36.dp)
                                .background(Color.Black, CircleShape)
                                .align(Alignment.BottomEnd)
                        ) {
                            Text(
                                text = "${day.number}",
                                color = Color.White,
                                modifier = Modifier.align(Alignment.Center),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Detail(day: Day) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(day.color)
    ) {

    }
}

@Preview
@Composable
private fun AdventCalendarPreview() {
    ComposePartyTheme {
        AdventCalendarSample()
    }
}

