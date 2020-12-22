import java.io.File

fun main(args: Array<String>) {
    try {
        println(dayXpart1())
    } catch (e: Exception) {
        println(e)
    }
}

private fun dayXpart1(): String {
    return readInput().toString()
}

private fun readInput(fileName: String = "src/main/resources/day_x_input"): List<String> {
    return File(fileName).readLines()
}