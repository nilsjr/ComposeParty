package de.nilsdruyen.composeparty.nyancat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.DefaultFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val entries = List(4) {
    "Text $it"
}

@Preview
@Composable
fun IsleExample() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        entries.forEach { Text(it, modifier = Modifier.padding(16.dp)) }
        Image(painter = rememberVectorPainter(image = isleHead), contentDescription = null)
    }

//    Canvas(modifier =Modifier.fillMaxSize().height(200.dp), onDraw = {
//
//
//
//    })
}

@Composable
fun Isle2Example() {
    Isle {
        Column {
            entries.forEach { Text(it, modifier = Modifier.padding(16.dp)) }
        }
    }
}

@Composable
fun Isle(modifier: Modifier = Modifier, children: @Composable () -> Unit) {
    Layout(children, modifier) { measurables, constraints ->
        layout(constraints.maxWidth, constraints.maxHeight) {

        }
    }
}

const val isleWidth = 200f
const val isleHeight = 40f

val isleHead = ImageVector.Builder(
    "isleHead",
    defaultWidth = isleWidth.dp,
    defaultHeight = isleHeight.dp,
    viewportWidth = isleWidth,
    viewportHeight = isleHeight,
).materialPath {
    moveTo(0f, 20f)

    quadTo(50f, 0f, 120f, 20f)



    quadTo(120f, 30f, isleWidth, 20f)

//    lineTo(isleWidth, 10f)

    lineTo(isleWidth, isleHeight)
    lineTo(0f, isleHeight)


    close()
}.build()

inline fun ImageVector.Builder.materialPath(
    fillAlpha: Float = 1f,
    strokeAlpha: Float = 0f,
    pathFillType: PathFillType = DefaultFillType,
    pathBuilder: PathBuilder.() -> Unit
) = path(
    fill = SolidColor(Color.Black),
    fillAlpha = fillAlpha,
    stroke = null,
    strokeAlpha = strokeAlpha,
    strokeLineWidth = 1f,
    strokeLineCap = StrokeCap.Butt,
    strokeLineJoin = StrokeJoin.Bevel,
    strokeLineMiter = 1f,
    pathFillType = pathFillType,
    pathBuilder = pathBuilder
)