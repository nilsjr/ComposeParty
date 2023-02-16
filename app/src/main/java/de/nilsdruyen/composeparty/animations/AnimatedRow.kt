package de.nilsdruyen.composeparty.animations

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedRow(modifier: Modifier = Modifier) {
    var hide by remember { mutableStateOf(false) }
    val weight by animateFloatAsState(
        targetValue = if (hide) 0f else 1f,
        animationSpec = tween(durationMillis = 1000),
    )
    val alpha by animateFloatAsState(
        targetValue = if (hide) 0f else 1f,
        animationSpec = tween(durationMillis = 300),
    )

    val itemModifier =
        Modifier
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(2.dp))
            .padding(6.dp)

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.align(Alignment.Center)) {
            AnimatedContent(targetState = hide) { hideElements ->
                Row(
                    modifier
                        .clickable { hide = !hide }
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!hideElements) {
                        Text(
                            "Links",
                            modifier = Modifier
                                .weight(1f)
                                .alpha(alpha)
                                .then(itemModifier),
                            maxLines = 1,
                        )
                        Text(
                            "Mitte",
                            modifier = Modifier
                                .weight(1f)
                                .alpha(alpha)
                                .then(itemModifier),
                            maxLines = 1,
                        )
                    }
                    Text(
                        "$hide", modifier = Modifier
                            .weight(1f)
                            .then(itemModifier)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier
                    .clickable { hide = !hide }
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (weight != 0f) {
                    Text(
                        "${weight}",
                        modifier = Modifier
                            .weight(weight)
                            .then(itemModifier),
                        maxLines = 1,
                    )
                    Text(
                        "Mitte",
                        modifier = Modifier
                            .weight(weight)
                            .then(itemModifier),
                        maxLines = 1,
                    )
                }
                Text(
                    "${alpha}", modifier = Modifier
                        .weight(1f)
                        .then(itemModifier)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AnimatedRowPreview() {
    ComposePartyTheme {
        AnimatedRow()
    }
}