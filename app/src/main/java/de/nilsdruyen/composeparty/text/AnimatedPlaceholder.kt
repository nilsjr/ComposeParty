package de.nilsdruyen.composeparty.text

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun AnimatedPlaceholder(
    hints: List<String>,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    val iterator = hints.listIterator()

    val target by produceState(initialValue = hints.first()) {
        iterator.doWhenHasNextOrPrevious {
            value = it
        }
    }

    AnimatedContent(
        targetState = target,
        transitionSpec = { ScrollAnimation() }
    ) { str ->
        Text(
            text = str,
            color = textColor,
            fontSize = 14.withScale(),
        )
    }
}

@Composable
fun Int.withScale(): TextUnit {
    return with(LocalDensity.current) {
        (this@withScale / fontScale).sp
    }
}

suspend fun <T> ListIterator<T>.doWhenHasNextOrPrevious(
    delayMills: Long = 3000,
    doWork: suspend (T) -> Unit
) {
    while (hasNext() || hasPrevious()) {
        while (hasNext()) {
            delay(delayMills)
            doWork(next())
        }
        while (hasPrevious()) {
            delay(delayMills)
            doWork(previous())
        }
    }
}

object ScrollAnimation {

    operator fun invoke(): ContentTransform {
        return slideInVertically(
            initialOffsetY = { 50 },
            animationSpec = tween()
        ) + fadeIn() togetherWith  slideOutVertically(
            targetOffsetY = { -50 },
            animationSpec = tween()
        ) + fadeOut()
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
) {
    TextField(
        modifier = modifier,
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            AnimatedPlaceholder(
                hints = listOf(
                    "Text 1",
                    "Text 2",
                    "Search your favourite topic",
                ),
            )
        },
    )
}