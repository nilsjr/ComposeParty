package de.nilsdruyen.composeparty.layouts

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import de.nilsdruyen.composeparty.data.images

val default = Screen.Grid(images)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedElementSample(modifier: Modifier = Modifier) {
    var state by remember { mutableStateOf<Screen>(default) }

    BackHandler(state is Screen.Detail) {
        state = default
    }

    SharedTransitionLayout(modifier) {
        AnimatedContent(
            targetState = state,
            transitionSpec = {
                if (initialState is Screen.Grid) {
                    slideInHorizontally { -it } + fadeIn() togetherWith scaleOut(
                        tween(),
                        .7f
                    ) + fadeOut()
                } else {
                    scaleIn(
                        spring(),
                        .7f
                    ) + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
                }
            },
            label = ""
        ) { screen ->
            when (screen) {
                is Screen.Detail -> Detail(
                    image = screen.item,
                    animateContextScope = this,
                )

                is Screen.Grid -> Grid(
                    images = screen.items,
                    animateContextScope = this,
                    onImageClick = { state = Screen.Detail(it) }
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.Grid(
    images: List<String>,
    animateContextScope: AnimatedContentScope,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(140.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(images) { image ->
            AsyncImage(
                model = image,
                contentDescription = null,
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = "img-$image"),
                        animateContextScope,
                    )
                    .clickable { onImageClick(image) },
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.Detail(
    image: String,
    animateContextScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
) {
    Box(Modifier.fillMaxSize()) {
        AsyncImage(
            model = image,
            contentDescription = null,
            modifier = modifier
                .align(Alignment.Center)
                .sharedElement(
                    rememberSharedContentState(key = "img-$image"),
                    animateContextScope,
                )
        )
    }
}

sealed interface Screen {

    data class Grid(val items: List<String>) : Screen
    data class Detail(val item: String) : Screen
}
