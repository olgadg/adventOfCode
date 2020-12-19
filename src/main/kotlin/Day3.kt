import java.io.File

fun main(args: Array<String>) {
    try {
        println(day3part2())
    } catch (e: Exception) {
        println(e)
    }
}

private fun day3part1(): String {
    val input = readInput()
    return input.slope2().toString()
}

private fun day3part2(): Long {
    val input = readInput()
    return input.slope1() *
            input.slope2() *
            input.slope3() *
            input.slope4() *
            input.slope5()
}

private fun List<String>.slope1(): Long {
    val steps = (size - 1)
    return (0..steps).map { x ->
        getChar(x, x)
    }.count { it.isTree() }.toLong()
}

private fun List<String>.slope2(): Long {
    val steps = (size - 1)
    return (0..steps).map { x ->
        getChar(x, x * 3)
    }.count { it.isTree() }.toLong()
}

private fun List<String>.slope3(): Long {
    val steps = (size - 1)
    return (0..steps).map { x ->
        getChar(x, x * 5)
    }.count { it.isTree() }.toLong()
}

private fun List<String>.slope4(): Long {
    val steps = (size - 1)
    return (0..steps).map { x ->
        getChar(x, x * 7)
    }.count { it.isTree() }.toLong()
}

private fun List<String>.slope5(): Long {
    val steps = (size - 1) / 2
    return (0..steps).map { y ->
        getChar(y * 2, y)
    }.count { it.isTree() }.toLong()
}

private fun List<String>.getChar(x: Int, y: Int): Char {
    return this[x][getYPosition(y)]
}

private fun getYPosition(y: Int): Int {
    return y.rem(MAX_Y - 1)
}

private fun Char.isTree(): Boolean {
    return this == '#'
}

private fun readInput(): List<String> {
    val file = File("src/main/resources/day_3_input")
    return file.readLines()
}

private const val MAX_Y = 32