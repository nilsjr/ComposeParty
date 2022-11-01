import kotlin.math.absoluteValue

data class Tick(val a: Float, val alpha: Float, val f: Float, val sF: Float)

fun lerp(a: Float, b: Float, f: Float) = a + f * (b - a)

val fraction = .35f
val angle = (fraction % 1f) * 360f
val pointerFraction = (fraction - 1f).absoluteValue
val boxAmount = 12
val angleStep = 360f / boxAmount
val startAtFraction = 1f / boxAmount
val boxes = List(boxAmount) {
    val tickAngle = it * angleStep
    val tickStartAt = 1f - startAtFraction * it
    val alpha = if (tickAngle < angle) 1f else .3f
    val tickFraction = (fraction + tickStartAt).coerceIn(0f..1f)
    Tick(tickAngle, alpha, tickFraction, tickStartAt)
}
println("fraction: $fraction - $angle - $pointerFraction")
boxes.forEach { println(it) }