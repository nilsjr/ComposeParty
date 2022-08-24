package de.nilsdruyen.composeparty.buttons

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.paths.PawAnimatedIcon
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@Composable
fun LoadingButtonDemo() {
    val loading = remember { mutableStateOf(false) }

    val onClick: () -> Unit = {
        loading.value = !loading.value
    }
    Column(modifier = Modifier.padding(16.dp)) {
        LoadingButton(
            modifier = Modifier.fillMaxWidth(),
            isIdle = !loading.value,
            onClick = onClick,
        )
        Spacer(modifier = Modifier.height(16.dp))
        ComposePartyTheme(false) {
            LoadingButton(
                modifier = Modifier.fillMaxWidth(),
                isIdle = !loading.value,
                onClick = onClick,
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoadingButton(modifier: Modifier = Modifier, isIdle: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(56.dp)
            .then(modifier)
    ) {
        AnimatedContent(
            targetState = isIdle,
            transitionSpec = {
                fadeIn(animationSpec = tween(150, 150)) with
                        fadeOut(animationSpec = tween(150)) using
                        SizeTransform { initialSize, targetSize ->
                            if (targetState) {
                                keyframes {
                                    // Expand horizontally first.
                                    IntSize(targetSize.width, initialSize.height) at 150
                                    durationMillis = 300
                                }
                            } else {
                                keyframes {
                                    // Shrink vertically first.
                                    IntSize(initialSize.width, targetSize.height) at 150
                                    durationMillis = 300
                                }
                            }
                        }
            }
        ) { isIdle ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                if (isIdle) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                    Text("Click me", Modifier.align(Alignment.Center))
                } else {
                    PawAnimatedIcon(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewLoadingButton() {
    LoadingButtonDemo()
}