import java.io.File
import java.util.*

fun main(args: Array<String>) {
    try {
        println(day6part1())
        println(day6part2())
    } catch (e: Exception) {
        println(e)
    }
}

private fun day6part1(): Int {
    return readInput().sumBy { it.countAnswers() }
}

private fun day6part2(): Int {
    return readInput().sumBy { it.byGroup().countAnswers() }
}

private fun readInput(): List<String> {
    val file = File("src/main/resources/day_6_input")

    val scanner = Scanner(file).useDelimiter("\\n\\n")
    return scanner.iterator().asSequence().toList()
}

private fun String.byGroup(): List<String> {
    return split('\n')
}

private fun String.countAnswers(): Int {
    return ('a'..'z').count { this.contains(it) }
}

private fun List<String>.countAnswers(): Int {
    return ('a'..'z').count { char -> this.all { it.contains(char) } }
}