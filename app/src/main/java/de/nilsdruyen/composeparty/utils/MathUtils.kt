package de.nilsdruyen.composeparty.utils

fun lerp(min: Float, max: Float, norm: Float): Float = (max - min) * norm + min

fun lerp(min: Int, max: Int, norm: Int): Int = (max - min) * norm + min

fun norm(value: Float, min: Float, max: Float): Float {
    return (value - min) / (max - min)
}

fun map(
    value: Float,
    sourceMin: Float,
    sourceMax: Float,
    destMin: Float,
    destMax: Float
): Float {
    return lerp(
        norm = norm(value = value, min = sourceMin, max = sourceMax),
        min = destMin,
        max = destMax
    )
}