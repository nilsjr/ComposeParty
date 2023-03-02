package de.nilsdruyen.composeparty.freestyle

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.pullRefreshIndicatorTransform
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.paths.PawAnimatedIcon
import de.nilsdruyen.composeparty.paths.PawIcon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val colors = listOf(
    Color.LightGray,
    Color.Gray,
    Color.DarkGray,
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullRefreshSample() {
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    var itemCount by remember { mutableStateOf(10) }

    fun refresh() = refreshScope.launch {
        refreshing = true
        delay(2500)
        itemCount += 2
        refreshing = false
    }

    val state = rememberPullRefreshState(refreshing, ::refresh)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .pullRefresh(state)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            repeat(itemCount) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(colors.random())
                )
            }
        }

//        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
        PawRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PawRefreshIndicator(
    refreshing: Boolean,
    state: PullRefreshState,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
) {
    val showElevation by remember(refreshing, state) {
        derivedStateOf { refreshing || state.progress > 0.5f }
    }

    Surface(
        modifier = modifier
            .size(40.dp)
            .pullRefreshIndicatorTransform(state, false),
        shape = CircleShape,
        color = backgroundColor,
        elevation = if (showElevation) 8.dp else 0.dp,
    ) {
        Crossfade(
            targetState = refreshing,
            animationSpec = tween(durationMillis = 100),
            label = "crossfade",
        ) { refreshing ->
            Box(modifier = Modifier.fillMaxSize()) {
                val iconModifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Center)
                if (refreshing) {
                    PawAnimatedIcon(
                        modifier = iconModifier,
                        color = contentColor,
                    )
                } else {
                    PawIcon(
                        progress = state.progress,
                        modifier = iconModifier,
                        color = contentColor,
                    )
                }
            }
        }
    }
}
