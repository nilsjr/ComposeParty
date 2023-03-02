package de.nilsdruyen.composeparty.layouts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.Visibility
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@OptIn(ExperimentalMotionApi::class)
@Composable
fun ConstraintAnimation() {
    var visible by remember { mutableStateOf(true) }
    val progress by animateFloatAsState(targetValue = if (visible) 1f else 0f)

    Box(Modifier.fillMaxSize()) {
        Button(
            onClick = { visible = !visible },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            Text("Toggle", modifier = Modifier.testTag("button_text_1"))
        }

        MotionLayout(
            start = expandedSet(),
            end = collapsedSet(),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(16.dp)
                .background(Color.Yellow),
            progress = progress,
        ) {
            Text(
                text = "Hallo",
                modifier = Modifier
                    .layoutId("text")
                    .background(Color.Red)
            )
            Text(
                text = "Nils",
                modifier = Modifier
                    .layoutId("text2")
                    .background(Color.Blue)
            )
        }
    }
}

fun collapsedSet(): ConstraintSet {
    return ConstraintSet {
        val (text, text2) = createRefsFor("text", "text2")
        constrain(text) {
            linkTo(parent.top, parent.bottom, 16.dp, 16.dp)
            linkTo(parent.start, parent.end, 16.dp, 16.dp)
        }
        constrain(text2) {
            visibility = Visibility.Gone
            linkTo(text.bottom, parent.bottom, 16.dp, 16.dp)
            linkTo(text.start, text.end)
        }
    }
}

fun expandedSet(): ConstraintSet {
    return ConstraintSet {
        val (text, text2) = createRefsFor("text", "text2")
        constrain(text) {
            linkTo(parent.top, text2.top, 16.dp, 16.dp)
            linkTo(parent.start, parent.end, 16.dp, 16.dp)
        }
        constrain(text2) {
            linkTo(text.bottom, parent.bottom, 16.dp, 16.dp)
            linkTo(text.start, text.end)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ConstraintAnimationPreview() {
    ComposePartyTheme {
        ConstraintAnimation()
    }
}