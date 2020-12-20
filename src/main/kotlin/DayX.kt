import java.io.File

fun main(args: Array<String>) {
    try {
        println(day9part1())
    } catch (e: Exception) {
        println(e)
    }
}

private fun day9part1(): String {
    return readInput().toString()
}

private fun readInput(): List<String> {
    val file = File("src/main/resources/day_x_input")
    return file.readLines()
}