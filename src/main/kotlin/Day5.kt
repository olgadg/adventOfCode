import java.io.File
import java.lang.IllegalArgumentException

fun main(args: Array<String>) {
    try {
        println(day5part1())
        println(day5part2())
    } catch (e: Exception) {
        println(e)
    }
}

private fun day5part1(): Int {
    return readInput().map {
        it.getRow() * 8 + it.getColumn()
    }.max()!!
}

private fun day5part2(): List<Int> {
    val seatIds = readInput().map {
        it.getRow() * 8 + it.getColumn()
    }
    return (0..901).filter { !seatIds.contains(it) }
}

private fun day5part1Example(): Pair<Int, Int> {
    val string = "BBFFBBFRLL"
    return string.getRow() to string.getColumn()
}

private fun readInput(): List<String> {
    val file = File("src/main/resources/day_5_input")
    return file.readLines()
}


private fun String.getRow(): Int {
    var min = 0
    var max = 127
    (0..5).forEach {
        val char = get(it)
        when (char) {
            'B', 'R' -> min += (max - min) / 2 + 1
            'F', 'L' -> max -= (max - min) / 2 + 1
        }
        println("char=$char min=$min max=$max")
    }
    return when (get(6)) {
        'B', 'R' -> max
        'F', 'L' -> min
        else -> error("")
    }
}


private fun String.getColumn(): Int {
    var min = 0
    var max = 7
    (7..8).forEach {
        val char = get(it)
        when (char) {
            'B', 'R' -> min += (max - min) / 2 + 1
            'F', 'L' -> max -= (max - min) / 2 + 1
        }
        println("char=$char min=$min max=$max")
    }
    return when (get(9)) {
        'B', 'R' -> max
        'F', 'L' -> min
        else -> error("")
    }
}

private const val MAX_ROW = 128