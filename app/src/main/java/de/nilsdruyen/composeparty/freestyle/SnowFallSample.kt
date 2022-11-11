package de.nilsdruyen.composeparty.freestyle

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import de.nilsdruyen.composeparty.R
import de.nilsdruyen.composeparty.modifiers.snowfall

@Preview
@Composable
fun SnowFallSample() {
    val context = LocalContext.current
    val snowflakes = listOfNotNull(
        context.getDrawable(R.drawable.snowflake01),
        context.getDrawable(R.drawable.snowflake02),
        context.getDrawable(R.drawable.snowflake03),
        context.getDrawable(R.drawable.snowflake04),
    ).map {
        it.apply { setBounds(0, 0, intrinsicWidth, intrinsicHeight) }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_forest),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .withOverlay(
                    brush = Brush.verticalGradient(
                        .00f to Color(0x00000000),
                        .5f to Color(0x0A000000),
                        .9f to Color(0x7A000000),
                        1f to Color(0xE5000000),
                    )
                )
                .snowfall(snowflakes)
        )
    }
}

//internal fun Modifier.snowfall(
//
//) = composed {
//    var snowflakesState by remember {
//        mutableStateOf(SnowflakesState(-1, IntSize(0, 0)))
//    }
//
//    LaunchedEffect(Unit) {
//        while (isActive) {
//            withFrameNanos { newTick ->
//                val elapsedMillis =
//                    (newTick - snowflakesState.tickNanos).nanoseconds.inWholeMilliseconds
//                val wasFirstRun = snowflakesState.tickNanos < 0
//                snowflakesState.tickNanos = newTick
//
//                if (wasFirstRun) return@withFrameNanos
//                for (snowflake in snowflakesState.snowflakes) {
//                    snowflake.update(elapsedMillis)
//                }
//            }
//        }
//    }
//
//    onSizeChanged { newSize -> snowflakesState = snowflakesState.resize(newSize) }
//        .clipToBounds()
//        .drawWithContent {
//            drawContent()
//            snowflakesState.draw(drawContext.canvas)
//        }
//}
//
//fun ClosedRange<Float>.random() =
//    ThreadLocalRandom.current().nextFloat() * (endInclusive - start) + start
//
//fun Float.random() =
//    ThreadLocalRandom.current().nextFloat() * this
//
//fun Int.random() =
//    ThreadLocalRandom.current().nextInt(this)
//
//fun IntSize.randomPosition() =
//    Offset(width.random().toFloat(), height.random().toFloat())
//
//private const val snowflakeDensity = 0.1
//private val incrementRange = 0.4f..0.8f
//private val sizeRange = 5.0f..12.0f
//private const val angleSeed = 25.0f
//private val angleSeedRange = -angleSeed..angleSeed
//private const val angleRange = 0.1f
//private const val angleDivisor = 10000.0f
//const val baseFrameDurationMillis = 16
//const val baseSpeedPxAt60Fps = 3.5f
//
//internal data class SnowflakesState(
//    var tickNanos: Long,
//    val snowflakes: List<Snowflake>,
//) {
//
//    constructor(tick: Long, canvasSize: IntSize) : this(tick, createSnowflakes(canvasSize))
//
//    fun draw(canvas: Canvas) {
//        snowflakes.forEach { it.draw(canvas) }
//    }
//
//    fun resize(newSize: IntSize) = copy(snowflakes = createSnowflakes(newSize))
//
//    companion object {
//
//        private fun createSnowflakes(canvasSize: IntSize): List<Snowflake> {
//            val canvasArea = canvasSize.width * canvasSize.height
//            val normalizedDensity = snowflakeDensity.coerceIn(0.0..1.0) / 500.0
//            val snowflakesCount = (canvasArea * normalizedDensity).roundToInt()
//
//            return List(snowflakesCount) {
//                Snowflake(
//                    incrementFactor = incrementRange.random(),
//                    size = sizeRange.random(),
//                    canvasSize = canvasSize,
//                    position = canvasSize.randomPosition(),
//                    angle = angleSeed.random() / angleSeed * angleRange + (PI / 2.0) - (angleRange / 2.0)
//                )
//            }
//        }
//    }
//}
//
//private val snowflakePaint = Paint().apply {
//    isAntiAlias = true
//    color = Color.White
//    style = PaintingStyle.Fill
//}
//
//internal class Snowflake(
//    private val incrementFactor: Float,
//    private val size: Float,
//    private val canvasSize: IntSize,
//    position: Offset,
//    angle: Double
//) {
//
//    private var position by mutableStateOf(position)
//    private var angle by mutableStateOf(angle)
//
//    fun update(elapsedMillis: Long) {
//        val increment =
//            incrementFactor * (elapsedMillis / baseFrameDurationMillis) * baseSpeedPxAt60Fps
//
//        val xDelta = (increment * cos(angle)).toFloat()
//        val yDelta = (increment * sin(angle)).toFloat()
//        position = Offset(position.x + xDelta, position.y + yDelta)
//
//        angle += angleSeedRange.random() / angleDivisor
//
//        if (position.y > canvasSize.height + size) {
//            position = Offset(position.x, -size)
//        }
//    }
//
//    fun draw(canvas: Canvas) {
//        canvas.drawCircle(center = position, radius = size, paint = snowflakePaint)
//    }
//}