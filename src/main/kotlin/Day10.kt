import java.io.File

fun main(args: Array<String>) {
    try {
        println(day10part1Example1())
        println(day10part1Example2())
        println(day10part1())
        println(day10part2Example1())
        println(day10part2Example2())
        println(day10part2())
    } catch (e: Exception) {
        println(e.printStackTrace())
    }
}

private fun day10part1(): String {
    return readInput().joltJumps()
}

private fun day10part1Example1(): String {
    return readInput("src/main/resources/day_10_input_example").joltJumps()
}

private fun day10part1Example2(): String {
    return readInput("src/main/resources/day_10_input_example_2").joltJumps()
}

private fun day10part2(): String {
    return readInput().joltPathSort().count3().toString()
}

private fun day10part2Example1(): String {
    return readInput("src/main/resources/day_10_input_example")
            .joltPathSort().count3().toString()
}

private fun day10part2Example2(): String {
    return readInput("src/main/resources/day_10_input_example_2")
            .joltPathSort().count3().toString()
}

private fun List<Int>.joltJumps(): String {
    val joltPath = joltPathSort()
    println(joltPath)
    val jumps = joltPath.countJumps(1) to joltPath.countJumps(3)
    println(jumps)
    return (jumps.first * jumps.second).toString()
}

private fun readInput(): List<Int> {
    return readInput("src/main/resources/day_10_input")
}

private fun readInput(input: String): List<Int> {
    val file = File(input)
    return file.readLines().map { it.toInt() }
}

private fun List<Int>.joltPathSort(): List<Int> {
    return listOf(0).plus(this).sorted().plus(max()!! + 3)
}

private fun List<Int>.countJumps(diff: Int): Int {
    val jumps = filterIndexed { index, i ->
        index > 0 && i == get(index - 1) + diff
    }
    return jumps.count()
}

private fun List<Int>.toJumps(): List<Int> {
    val jumps = mapIndexed { index, value ->
        if (index == 0) 0 else (value - get(index - 1))
    }.subList(1, size - 1)
    return jumps
}

private fun List<Int>.count3(index: Int = 0, results: HashMap<Int, Long> = HashMap()): Long {
    if (results[index] != null) {
        println("DONE: address=$index results=${results[index]}")

        return results[index]!!
    }
    var count = 0L
    if (index == size - 1) {
        count += 1
        results[index] = count
        return count
    }
    if (index <= size - 2) {
        if (get(index + 1) - get(index) <= 3) {
            count += results[index + 1] ?: count3(index + 1, results)
        }
    }
    if (index <= size - 3) {
        if (get(index + 2) - get(index) <= 3) {
            count += results[index + 2] ?: count3(index + 2, results)
        }
    }
    if (index <= size - 4) {
        if (get(index + 3) - get(index) <= 3) {
            count += results[index + 3] ?: count3(index + 3, results)
        }
    }
    results[index] = count
    println("address=$index results=$count")
    return count
}