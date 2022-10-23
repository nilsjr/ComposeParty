package de.nilsdruyen.composeparty.swipe

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

val Arrows = listOf(Arrow1, Arrow2)
val Angles = listOf(Angle.Top, Angle.Right, Angle.Bottom, Angle.Left)

interface GameEngine {

    fun initialGame(): Game

    suspend fun randomState(): Game

    suspend fun performAction(swipeAction: Angle)
}

class GameEngineImpl : GameEngine {

    companion object {

        const val MAX_SIZE = 8
    }

    var size = 2

    override fun initialGame() = Game(
        Config(
            size,
            size,
            Background,
            Arrows,
            Angle.Top,
            Angle.Right,
            randomPosition()
        )
    )

    override suspend fun randomState(): Game {
        return withContext(Dispatchers.Default) {
            val angle = Angles.random()
            Game(Config(size, size, Background, Arrows, angle, keyAngle(angle), randomPosition()))
        }
    }

    override suspend fun performAction(swipeAction: Angle) {
        when (swipeAction) {
            Angle.Top -> increaseSize()
            Angle.Right -> increaseSize()
            Angle.Bottom -> increaseSize()
            Angle.Left -> increaseSize()
        }
    }

    private fun increaseSize() {
        if (size == MAX_SIZE) return
        size++
    }

    private fun randomPosition() = Position(Random.nextInt(size), Random.nextInt(size))

    private fun keyAngle(angle: Angle): Angle {
        val keyAngle = Angles.random()
        if (keyAngle == angle) return keyAngle(angle)
        return keyAngle
    }
}

data class Game(val config: Config) {

    fun buildArrows(): List<Arrow> {
        return with(config) {
            mutableListOf<Arrow>().apply {
                var index = 0
                repeat(rowCount) { row ->
                    repeat(columnCount) { col ->
                        val color = config.arrowColors[index % 2]
                        if (keyArrowPosition.x == col && keyArrowPosition.y == row) {
                            add(Arrow(keyArrowDirection, color, false))
                        } else {
                            add(Arrow(arrowDirection, color, false))
                        }
                        index++
                    }
                }
            }
        }
    }
}

data class Config(
    val rowCount: Int,
    val columnCount: Int,
    val backgroundColor: Long,
    val arrowColors: List<Long>,
    val arrowDirection: Angle,
    val keyArrowDirection: Angle,
    val keyArrowPosition: Position,
    val animationProbability: Float = 0f,
    val animateType: Int = 0,
)

data class Arrow(
    val direction: Angle,
    val color: Long,
    val animationEnabled: Boolean,
    val animationProps: AnimationProps = AnimationProps(0, 0),
)

data class AnimationProps(
    val type: Int,
    val delayMs: Int,
)

data class Position(val x: Int, val y: Int)

sealed class Angle(val rotationZ: Float) {

    object Top : Angle(90f)
    object Right : Angle(180f)
    object Bottom : Angle(270f)
    object Left : Angle(0f)
}