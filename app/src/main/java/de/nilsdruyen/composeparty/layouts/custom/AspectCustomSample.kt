package de.nilsdruyen.composeparty.layouts.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@Composable
fun AspectCustomSample(modifier: Modifier = Modifier) {
//    val density = LocalDensity.current
//    var size by remember { mutableStateOf(86.dp) }

    ConstraintLayout {
        val (bg, content) = createRefs()

        Box(
            Modifier
                .constrainAs(bg) {
                    height = Dimension.fillToConstraints
                    width = Dimension.ratio("1:1")
                    linkTo(parent.top, parent.bottom)
                }
                .clip(CircleShape)
                .background(Color.Red)
        )
        Column(Modifier.constrainAs(content) {
            linkTo(parent.top, parent.bottom)
            linkTo(parent.start, parent.end)
        }) {
            Text("Nils")
            Text("Nils")
            Text("Nils")
            Text("Nils")
            Text("Nils")
            Text("Nils")
            Text("Nils")
        }
    }
//    Box(Modifier.width(IntrinsicSize.Min)) {
//        Box(modifier.matchParentSize())
//        Column(
//            Modifier
//                .wrapContentSize()
//                .onGloballyPositioned {
//                    val newValue = maxOf(it.size.width, it.size.height)
//                    with(density) {
//                        size = newValue.toDp()
//                    }
//                }
//                .clip(CircleShape)
//                .background(Color.Red)
//        ) {
//            Text("Nils")
//            Text("Nils")
//            Text("Nils")
//            Text("Nils")
//            Text("Nils")
//            Text("Nils")
//            Text("Nils")
//        }
//    }
}

@Preview
@Composable
private fun AspectCustomSamplePreview() {
    ComposePartyTheme {
        AspectCustomSample()
    }
}
