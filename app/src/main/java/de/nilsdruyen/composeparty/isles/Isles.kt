package de.nilsdruyen.composeparty.isles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp

@Composable
fun Isle(color: Color, modifier: Modifier = Modifier, block: @Composable ColumnScope.() -> Unit) {
    Column(modifier = modifier.fillMaxWidth()) {
        Icon(
            painter = rememberVectorPainter(image = isleHead),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio),
            tint = color,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color), content = block
        )
        Icon(
            painter = rememberVectorPainter(image = isleFooter),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio),
            tint = color,
        )
    }
}

const val isleWidth = 100f
const val isleHeight = 15f
const val ratio = isleWidth / isleHeight

private val isleHead = ImageVector.Builder(
    "isleHead",
    defaultWidth = isleWidth.dp,
    defaultHeight = isleHeight.dp,
    viewportWidth = isleWidth,
    viewportHeight = isleHeight,
).customPath {
    moveTo(0f, isleHeight)
    quadTo(isleWidth * 0.15f, isleHeight * 0.9f, isleWidth * 0.3f, isleHeight * 0.6f)
    quadTo(isleWidth * 0.6f, 0f, isleWidth, isleHeight * 0.75f)
    lineTo(isleWidth, isleHeight)
    lineTo(0f, isleHeight)
    close()
}.build()

private val isleFooter = ImageVector.Builder(
    "isleFooter",
    defaultWidth = isleWidth.dp,
    defaultHeight = isleHeight.dp,
    viewportWidth = isleWidth,
    viewportHeight = isleHeight,
).customPath {
    moveTo(0f, 0f)
    lineTo(0f, isleHeight * 0.3f)
    quadTo(isleWidth * 0.3f, isleHeight * 1f, isleWidth * 0.6f, isleHeight * 0.6f)
    quadTo(isleWidth * 0.9f, isleHeight * 0.15f, isleWidth, isleHeight * 0.2f)
    lineTo(isleWidth, 0f)
    close()
}.build()

private inline fun ImageVector.Builder.customPath(
    fillAlpha: Float = 1f,
    strokeAlpha: Float = 1f,
    pathFillType: PathFillType = DefaultFillType,
    pathBuilder: PathBuilder.() -> Unit
) = path(
    fill = SolidColor(Color(0xFF196428)),
    fillAlpha = fillAlpha,
    stroke = SolidColor(Color(0xFF196428)),
    strokeAlpha = strokeAlpha,
    strokeLineWidth = 1f,
    strokeLineCap = StrokeCap.Butt,
    strokeLineJoin = StrokeJoin.Bevel,
    strokeLineMiter = 1f,
    pathFillType = pathFillType,
    pathBuilder = pathBuilder
)