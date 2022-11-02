val count = 10
val countRange = 0 until count
val sizeRange = 1..9
val dots = Array(count) { IntArray(count) }

countRange.forEach { x ->
    countRange.forEach { y ->
        dots[x][y] = sizeRange.random()
    }
}

dots.forEach {
    println(it.map { it })
}