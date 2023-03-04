package de.nilsdruyen.composeparty.buttons

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme
import de.nilsdruyen.composeparty.utils.Centered
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddToCartButtonDemo() {
    var state by remember { mutableStateOf(AddToCartButtonState()) }

    Centered {
        AddToCartButton(
            state = state,
            toggle = {
                state = state.copy(isExpanded = !it)
            },
            collapse = {
                state = state.copy(isExpanded = false)
            },
            increase = {
                state = state.copy(count = state.count + 1)
            },
            decrease = {
                state = state.copy(count = state.count - 1)
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AddToCartPreview() {
    ComposePartyTheme {
        AddToCartButtonDemo()
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddToCartButton(
    state: AddToCartButtonState,
    toggle: (Boolean) -> Unit,
    collapse: () -> Unit,
    increase: () -> Unit,
    decrease: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(state) {
        if (state.isExpanded) {
            delay(2000)
            collapse()
        }
    }

    Box(
        modifier = Modifier
            .size(260.dp)
            .padding(8.dp)
            .then(modifier)
    ) {
        val height = 56.dp
        val cornerRadius: Dp by animateDpAsState(
            targetValue = if (state.isExpanded) 8.dp else height / 2,
            animationSpec = tween()
        )

        Button(
            onClick = { toggle(state.isExpanded) },
            shape = RoundedCornerShape(cornerRadius),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .height(height)
                .align(Alignment.BottomCenter),
        ) {
            AnimatedContent(
                targetState = state.isExpanded,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220, delayMillis = 90)) with
                            fadeOut(animationSpec = tween(90)) using
                            SizeTransform(false) { _, _ -> tween() }
                }
            ) { isExpanded ->
                if (isExpanded) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .repeatingClickable(
                                    interactionSource = interactionSource,
                                    enabled = state.count > 0,
                                ) { decrease() }
                                .wrapContentSize()
                                .clip(CircleShape),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Remove,
                                contentDescription = null,
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .background(
                                    MaterialTheme.colorScheme.onPrimaryContainer,
                                    CircleShape
                                )
                                .size(42.dp)
                        ) {
                            Text(
                                text = "${state.count}",
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .repeatingClickable(
                                    interactionSource = interactionSource,
                                    enabled = state.count < 100,
                                ) { increase() }
                                .wrapContentSize()
                                .clip(CircleShape),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                            )
                        }
                    }
                } else {
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier
                    )
                }
            }
        }
        AnimationComponent(count = state.count, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimationComponent(
    count: Int,
    modifier: Modifier = Modifier,
) {
    var firstRun by remember { mutableStateOf(true) }
    var visible by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    LaunchedEffect(count) {
        if (firstRun) {
            firstRun = false
        } else {
            visible = true
            delay(1500)
            visible = false
        }
    }

    Box(modifier = modifier.offset(y = (-80).dp)) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically {
                with(density) { 40.dp.roundToPx() }
            } + expandVertically(
                expandFrom = Alignment.Bottom
            ) + fadeIn(
                initialAlpha = 0.3f
            ) + scaleIn(),
            exit = slideOutVertically {
                with(density) { 40.dp.roundToPx() }
            } + shrinkVertically(
                shrinkTowards = Alignment.Bottom
            ) + fadeOut() + scaleOut()
        ) {
            Text(
                text = "$count",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier,
            )
        }
    }
}

data class AddToCartButtonState(
    val count: Int = 1,
    val isExpanded: Boolean = false,
)

fun Modifier.repeatingClickable(
    interactionSource: InteractionSource,
    enabled: Boolean,
    maxDelayMillis: Long = 600,
    minDelayMillis: Long = 100,
    delayDecayFactor: Float = .20f,
    onClick: () -> Unit
): Modifier = composed {
    val currentClickListener by rememberUpdatedState(onClick)
    val coroutineScope = rememberCoroutineScope()

    pointerInput(interactionSource, enabled) {
        awaitEachGesture {
            val down = awaitFirstDown(requireUnconsumed = false)
            val heldButtonJob = coroutineScope.launch {
                var currentDelayMillis = maxDelayMillis
                while (enabled && down.pressed) {
                    currentClickListener()
                    delay(currentDelayMillis)
                    val nextMillis =
                        currentDelayMillis - (currentDelayMillis * delayDecayFactor)
                    currentDelayMillis = nextMillis.toLong().coerceAtLeast(minDelayMillis)
                }
            }
            waitForUpOrCancellation()
            heldButtonJob.cancel()
        }
    }
}