import java.io.File

fun main(args: Array<String>) {
    println(day2part2())
}

fun day2part1(): Int {
    return readInput().map { it.toPasswordPolicy() }.filter { it.matchesPart1() }.count()
}

fun day2part2(): Int {
    return readInput().map { it.toPasswordPolicy() }.filter { it.matchesPart2() }.count()
}

private fun readInput(): List<String> {
    val file = File("src/main/resources/day_2_input")
    return file.readLines()
}

private fun String.toPasswordPolicy(): PasswordPolicy {
    val match = Regex("(\\d*)-(\\d*) (\\w): (\\w*)").find(this)
    val (min, max, letter, password) = match!!.destructured
    return PasswordPolicy(
        min.toInt(),
        max.toInt(),
        letter[0],
        password
    )
}

private fun PasswordPolicy.matchesPart1(): Boolean {
    val count = password.count { it == letter }
    return count in firstNumber..secondNumber
}


private fun PasswordPolicy.matchesPart2(): Boolean {
    val position1Matches = password.getOrNull(firstNumber - 1) == letter
    val position2Matches = password.getOrNull(secondNumber - 1) == letter
    return position1Matches.xor(position2Matches)
}

data class PasswordPolicy(
    val firstNumber: Int,
    val secondNumber: Int,
    val letter: Char,
    val password: String
)