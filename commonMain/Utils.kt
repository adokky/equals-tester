package dev.adokky.eqtester

internal inline fun allPairs(range: IntRange, f: (Int, Int) -> Unit) {
    for (i in range) {
        for (j in range) {
            f(i, j)
        }
    }
}

internal inline fun randomPairs(range: IntRange, iterations: Int, f: (Int, Int) -> Unit) {
    repeat(iterations) {
        f(range.random(), range.random())
    }
}

internal inline fun allPairsExceptSelf(range: IntRange, f: (Int, Int) -> Unit) {
    allPairs(range) { i1, i2 ->
        if (i1 != i2) f(i1, i2)
    }
}

internal fun IntRange.randomPair(): Pair<Int, Int> {
    val i1 = random()
    var i2: Int
    do { i2 = random() } while (i2 == i1)
    return i1 to i2
}