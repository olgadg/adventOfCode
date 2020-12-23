import java.io.File

fun main(args: Array<String>) {
    try {
        println(dayXpart1example())
        println(dayXpart1())
    } catch (e: Exception) {
        println(e.printStackTrace())
    }
}

private fun dayXpart1(): String {
    return readInput().toString()
}

private fun dayXpart1example(): String {
    return readInput("src/main/resources/day_x_input_example").toString()
}

private fun readInput(fileName: String = "src/main/resources/day_x_input"): List<String> {
    return File(fileName).readLines()
}