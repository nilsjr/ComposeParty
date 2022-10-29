package de.nilsdruyen.composeparty.cards

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Preview
@Composable
fun TeaserSample() {
    val cardHeight = remember { mutableStateOf(200) }

    Box(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardHeight.value.dp)
            ) {
                val (illustration) = createRefs()
                Box(
                    modifier = Modifier
                        .constrainAs(illustration) {
                            width = Dimension.percent(.36f)
                            height = Dimension.fillToConstraints
                            linkTo(parent.top, parent.bottom)
                            start.linkTo(parent.start)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    val brush = Brush.verticalGradient(
                        listOf(
                            Color(0xFFB0D8F0),
                            Color(0xFFFFFFFF),
                            Color(0xFFB0D8F0),
                        )
                    )
                    Canvas(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val curveWidth = size.width * .25f
                        val widthMultiplier = 1.1f
                        val path = Path()
                        path.moveTo(0f, 0f)
                        path.lineTo(size.width - curveWidth, 0f)
                        path.cubicTo(
                            x1 = size.width * widthMultiplier,
                            y1 = size.height * .3f,
                            x2 = size.width * widthMultiplier,
                            y2 = size.height * .7f,
                            x3 = size.width - curveWidth,
                            y3 = size.height
                        )
                        path.lineTo(0f, size.height)

                        drawPath(
                            path = path,
                            brush = brush,
                        )
                    }
                }
            }
        }
        Slider(
            value = cardHeight.value.toFloat(),
            onValueChange = {
                cardHeight.value = it.toInt()
            },
            valueRange = 50f..500f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .align(Alignment.BottomCenter)
        )
    }
}