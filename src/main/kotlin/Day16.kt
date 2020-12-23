import java.io.File

fun main(args: Array<String>) {
    try {
        //println(day16part1example())
        //println(day16part1())
        println(day16part2example())
        println(day16part2())
    } catch (e: Exception) {
        println(e.printStackTrace())
    }
}

private fun day16part1(): String {
    return readInput()
            .part1()
            .sum()
            .toString()
}

private fun day16part1example(): String {
    return readInput("src/main/resources/day_16_input_example")
            .part1()
            .sum()
            .toString()
}

private fun day16part2(): String {
    return readInput()
            .part2()
            .toString()
}

private fun day16part2example(): String {
    return readInput("src/main/resources/day_16_input_example_2")
            .part2example()
            .toString()
}

private fun List<String>.part1(): List<Int> {
    val (rules, _, tickets) = parse()
    println("rules=$rules tickets=$tickets")
    return tickets.map { it.findErrors(rules) }.flatten()
}

private fun List<String>.part2example(): Map<Int, Set<String>> {
    val (rules, yourTicket, tickets) = parse()
    println("rules=$rules tickets=$tickets")
    return tickets.filter { it.matches(rules) }
            .map {
                val findPossibilities = it.findPossibilities(rules)
                //println("$index $findPossibilities")
                findPossibilities
            }.intersection()
            .reduceUntilDone()

}

private fun List<String>.part2(): Long {
    val (rules, yourTicket, tickets) = parse()
    println("rules=$rules tickets=$tickets")
    val meaning = tickets.filter { it.matches(rules) }
            .map {
                val findPossibilities = it.findPossibilities(rules)
                //println("$index $findPossibilities")
                findPossibilities
            }.intersection()
            .reduceUntilDone()
    println(meaning)
    val indexes = meaning.filter { (_, value) -> value.first().contains("departure") }.keys
    println(indexes)
    val values = yourTicket.filterIndexed { index, i -> indexes.contains(index) }.map { it.toLong() }
    println(values)
    return values.reduce { acc, element -> acc * element }

}


private fun readInput(fileName: String = "src/main/resources/day_16_input"): List<String> {
    return File(fileName).readLines()
}

private fun List<String>.parse(): Input {
    val rules = subList(0, indexOfFirst { it.isBlank() }).map { it.parseRule() }
    val tickets = subList(indexOf(LINE_TICKETS) + 1, size)
            .map { it.split(",").map { it.toInt() } }
    val yourTicket = get(indexOf(LINE_YOUR_TICKET) + 1).split(",").map { it.toInt() }
    return Input(rules, yourTicket, tickets)
}

private fun String.parseRule(): Rule {
    val match = Regex("(.*): (\\d*)-(\\d*) or (\\d*)-(\\d*)").find(this)
    val (name, minRange1, maxRange1, minRange2, maxRange2) = match!!.destructured

    return Rule(name, (minRange1.toInt()..maxRange1.toInt()), (minRange2.toInt()..maxRange2.toInt()))
}

private fun List<Int>.findErrors(rules: List<Rule>): List<Int> {
    return filterNot { it.matches(rules) }
}

private fun List<Int>.matches(rules: List<Rule>): Boolean {
    return all { it.matches(rules) }
}

private fun Int.matches(rules: List<Rule>): Boolean {
    return rules.any { this in it.range1 || this in it.range2 }
}

private fun Int.matchedRules(rules: List<Rule>): List<Rule> {
    return rules.filter { this in it.range1 || this in it.range2 }
}

private fun List<Int>.findPossibilities(rules: List<Rule>): Map<Int, Set<String>> {
    return mapIndexed { index, number -> index to number.matchedRules(rules).map { it.name }.toHashSet() }.toMap()
}

private fun List<Map<Int, Set<String>>>.intersection(): Map<Int, Set<String>> {
    return reduce { acc: Map<Int, Set<String>>, map ->
        map.map { (key, possibilities) ->
            key to acc[key]!!.intersect(possibilities)
        }.toMap()
    }
}

private fun Map<Int, Set<String>>.reduceUntilDone(): Map<Int, Set<String>> {
    var previous = emptyMap<Int, Set<String>>()
    var current = this
    while (previous != current) {
        val singles = current.filter { (_, value) -> value.size == 1 }
        previous = current
        current = previous.map { (index, value) ->
            index to value.filterNot { currentValue ->
                singles.filterKeys { it != index }.any { (_, value) -> value.first() == currentValue }
            }.toSet()
        }.toMap()
    }
    return current
}

fun List<HashSet<String>>.intersection(): HashSet<String> {
    return reduce { acc, it -> acc.apply { retainAll(it) } }
}

data class Rule(
        val name: String,
        val range1: IntRange,
        val range2: IntRange
)

data class Input(
        val rules: List<Rule>,
        val yourTicket: List<Int>,
        val tickets: List<List<Int>>
)

private const val LINE_YOUR_TICKET = "your ticket:"
private const val LINE_TICKETS = "nearby tickets:"