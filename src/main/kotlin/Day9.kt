import java.io.File

fun main(args: Array<String>) {
    try {
        //println(day9part1Example())
        println(day9part1())
        println(day9part2())
    } catch (e: Exception) {
        println(e)
    }
}

private fun day9part1(): String {
    return readInput().findOddOneWithFixedPreamble().toString()
}

private fun day9part1Example(): String {
    return readInputExample().findOddOneWithFixedPreamble().toString()
}

private fun day9part2(): String {
    return readInput().sums(ODD_NUMBER).minPlusMax().toString()
}

private fun readInput(): List<Long> {
    val file = File("src/main/resources/day_9_input")
    return file.readLines().map { it.toLong() }
}

private fun readInputExample(): List<Long> {
    val file = File("src/main/resources/day_9_input_example")
    return file.readLines().map { it.toLong() }
}

private fun List<Long>.findOddOneWithFixedPreamble(): Long {
    return find { value ->
        val index = indexOf(value)
        PREAMBLE < index && !subList(index - PREAMBLE - 1, index).matchesRule(value)
    } ?: error("Not found")
}

private fun List<Long>.matchesRule(number: Long): Boolean {

    forEach { firstNumber ->
        forEach { secondNumber ->
            if (secondNumber != firstNumber && number == firstNumber + secondNumber) {
                return true
            }
        }
    }
    return false
}

private fun List<Long>.sums(number: Long): List<Long> {

    val indexOfNumber = indexOf(number)
    (0 until indexOfNumber).forEach { indexFrom ->
        (indexFrom + 1 until indexOfNumber).forEach { indexTo ->
            val subL = subList(indexFrom, indexTo)
            if (subL.sum() == number) {
                return subL
            }

        }
    }
    error("Not found sum")
}

private fun List<Long>.minPlusMax() : Long {
    return min()!! + max()!!
}

private const val PREAMBLE = 25
private const val ODD_NUMBER = 22477624L