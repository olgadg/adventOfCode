import java.io.File

fun main(args: Array<String>) {
    try {
        //println(day11part1Example())
        //println(day11part1())
        println(day11part2Example())
        println(day11part2())
    } catch (e: Exception) {
        println(e)
    }
}

private fun day11part1(): String {
    return readInput().part1().toString()
}

private fun day11part1Example(): String {
    return readInput("src/main/resources/day_11_input_example")
            .part1()
            .toString()
}

private fun day11part2(): String {
    return readInput().part2().toString()
}


private fun day11part2Example(): String {
    return readInput("src/main/resources/day_11_input_example")
            .part2()
            .toString()
}

private fun readInput(fileName: String = "src/main/resources/day_11_input"): List<String> {
    return File(fileName).readLines()
}

private fun List<String>.part1(): Int {
    return applyRulesUntilStabilised(::applyRules).countOccupied()
}

private fun List<String>.part2(): Int {
    return applyRulesUntilStabilised(::applyRules2).countOccupied()
}

private fun List<String>.applyRulesUntilStabilised(rules: (List<String>) -> List<String>): List<String> {
    var previous = emptyList<String>()
    var current = this

    while (previous != current) {
        previous = current
        println(previous)
        current = rules(previous)
    }

    return current
}


private fun applyRules(list: List<String>): List<String> {
    return list.mapIndexed { x, row ->
        row.mapIndexed { y, seat ->
            val adjacents = list.getAdjacents(x, y)
            when (seat) {
                'L' -> if (adjacents.shouldOccupy()) '#' else 'L'
                '#' -> if (adjacents.shouldEmpty()) 'L' else '#'
                else -> seat
            }
        }.joinToString("")
    }
}

private fun List<Char>.shouldOccupy(): Boolean {
    return all { it != '#' }
}

private fun List<Char>.shouldEmpty(): Boolean {
    return count { it == '#' } >= 4
}


private fun applyRules2(list: List<String>): List<String> {
    return list.mapIndexed { y, row ->
        row.mapIndexed { x, seat ->
            val adjacents = list.getVisibleAdjacents(x, y)
            when (seat) {
                'L' -> if (adjacents.shouldOccupy()) '#' else 'L'
                '#' -> if (adjacents.shouldEmpty2()) 'L' else '#'
                else -> seat
            }
        }.joinToString("")
    }
}

private fun List<Char>.shouldEmpty2(): Boolean {
    return count { it == '#' } >= 5
}

private fun List<String>.countOccupied(): Int {
    return sumBy { it.count { it == '#' } }
}

private fun List<String>.getAdjacents(x: Int, y: Int): List<Char> {
    return listOfNotNull(
            if (x > 0 && y > 0) get(x - 1)[y - 1] else null,
            if (x > 0) get(x - 1)[y] else null,
            if (x > 0 && y < get(x - 1).length - 1) get(x - 1)[y + 1] else null,
            if (y > 0) get(x)[y - 1] else null,
            if (y < get(x).length - 1) get(x)[y + 1] else null,
            if (x < size - 1 && y > 0) get(x + 1)[y - 1] else null,
            if (x < size - 1) get(x + 1)[y] else null,
            if (x < size - 1 && y < get(x + 1).length - 1) get(x + 1)[y + 1] else null
    )
}

private fun List<String>.getVisibleAdjacents(x: Int, y: Int): List<Char> {
    //println("x=$x y=$y")
    val adjacents = listOf(
            diagonalUpLeft(x, y),
            upwards(x, y),
            diagonalUpRight(x, y),
            toLeft(x, y),
            toRight(x, y),
            diagonalDownLeft(x, y),
            downwards(x, y),
            diagonalDownRight(x, y)
    )
    //println(adjacents)
    return adjacents.filterNotNull()
}

private fun List<String>.toLeft(initialX: Int, initialY: Int): Char? {
    if (initialX == 0) {
        return null
    }
    return (initialX - 1 downTo 0)
            .map { x -> get(initialY)[x] }
            .find { it != '.' }
}

private fun List<String>.toRight(initialX: Int, initialY: Int): Char? {
    if (initialX == get(initialY).length - 1) {
        return null
    }
    return (initialX + 1 until  get(initialY).length).map { x ->
        get(initialY)[x]
    }
            .find { it != '.' }
}

private fun List<String>.upwards(initialX: Int, initialY: Int): Char? {
    if (initialY == 0) {
        return null
    }
    return (initialY - 1 downTo 0)
            .map { y -> get(y)[initialX] }
            .find { it != '.' }
}


private fun List<String>.downwards(initialX: Int, initialY: Int): Char? {
    if (initialY == size - 1) {
        return null
    }
    return (initialY + 1 until size)
            .map { y -> get(y)[initialX] }
            .find { it != '.' }
}

private fun List<String>.diagonalUpLeft(initialX: Int, initialY: Int): Char? {
    //println("diagonalUpLeft")
    return (1..Math.min(initialY, initialX))
            //.onEach { println("step $it") }
            .map { step -> get(initialY - step)[initialX - step] }
            .find { it != '.' }
}

private fun List<String>.diagonalUpRight(initialX: Int, initialY: Int): Char? {
    //println("diagonalUpRight")
    return (1..Math.min(initialY, get(initialY).length - 1 - initialX))
            //.onEach { println("step $it") }
            .map { step -> get(initialY - step)[initialX + step] }
            .find { it != '.' }
}

private fun List<String>.diagonalDownLeft(initialX: Int, initialY: Int): Char? {
    //println("diagonalDownLeft")
    return (1..Math.min(size - 1 - initialY, initialX))
            //.onEach { println("step $it") }
            .map { step -> get(initialY + step)[initialX - step] }
            .find { it != '.' }
}

private fun List<String>.diagonalDownRight(initialX: Int, initialY: Int): Char? {
    //println("diagonalDownRight")
    return (1..Math.min(size - 1 - initialY, get(initialY).length - 1 - initialX))
            //.onEach { println("step $it") }
            .map { step -> get(initialY + step)[initialX + step] }
            .find { it != '.' }
}