import java.io.File
import java.util.*

fun main(args: Array<String>) {
    try {
        println(day4part1())
        println(day4part2())
        println(day4part2().count())
    } catch (e: Exception) {
        println(e)
    }
}

private fun day4part1(): String {
    return readInput().filter { info ->
        REQUIRED_FIELDS.count { s -> info.contains(s) } == REQUIRED_FIELDS.size
    }.toList().toString()
}

private fun day4part2(): List<String> {
    return readInput().filter { info ->
        info.matchesBirthYear() &&
                info.matchesIssueYear() &&
                info.matchesExpirationYear() &&
                info.matchesHeight() &&
                info.matchesHairColor() &&
                info.matchesEyeColor() &&
                info.matchesPassportId()
    }.toList().mapNotNull { it.getPid() }
}

private fun readInput(): Sequence<String> {
    val file = File("src/main/resources/day_4_input")
    val scanner = Scanner(file).useDelimiter("\\n\\n")
    return scanner.iterator().asSequence().map { it.replace(Regex("\\n"), " ") + " "}
}

private fun String.matchesBirthYear(): Boolean {
    return matchesFourDigits("byr", 1920, 2002)
}

private fun String.matchesIssueYear(): Boolean {
    return matchesFourDigits("iyr", 2010, 2020)
}

private fun String.matchesExpirationYear(): Boolean {
    return matchesFourDigits("eyr", 2020, 2030)
}

private fun String.matchesHeight(): Boolean {
    val regexCm = Regex("hgt:(\\d*)cm")
    val resultCm = getResultInt(regexCm)?.let { it in 150..193 } == true

    val regexIn = Regex("hgt:(\\d*)in")
    val resultIn = getResultInt(regexIn)?.let { it in 59..76 } == true
    return resultCm || resultIn
}

private fun String.matchesHairColor(): Boolean {
    val regex = Regex("hcl:\\#[a-f0-9]{6}")
    return regex.containsMatchIn(this)
}

private fun String.matchesEyeColor(): Boolean {
    val regex = Regex("ecl:(amb|blu|brn|gry|grn|hzl|oth)")
    return regex.containsMatchIn(this)
}

private fun String.matchesPassportId(): Boolean {
    val regex = Regex("pid:\\d{9}\\s")
    return regex.containsMatchIn(this)
}

private fun String.matchesFourDigits(field: String, min: Int, max: Int): Boolean {
    val regex = Regex("$field:(\\d{4})")
    val resultInt = getResultInt(regex)
    return resultInt?.let { it in min..max } == true
}

private fun String.getResultInt(regex: Regex) : Int? {
    return regex.find(this)?.let {
        val (result) = it.destructured
        result?.toInt()
    }
}


private fun String.getPid() : String? {
    val regex = Regex("pid:(\\d{9})")
    return regex.find(this)?.let {
        val (result) = it.destructured
        result
    }
}


private val REQUIRED_FIELDS = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")