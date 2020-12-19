import java.io.File
import java.util.*

fun main(args: Array<String>) {
    try {
        println(day7part1())
        println(day7part1().size)
        println(day7part2Example())
        println(day7part2())
    } catch (t: Throwable) {
        println(t)
    }
}

private fun day7part1(): Set<String> {
    return readInput()
        .map { it.toRule() }
        .toMap()
        .canContain("shiny gold bag")
}

private fun day7part2(): Int {
    return readInput()
        .map { it.toRule() }
        .toMap()
        .contentsTotal("shiny gold bag")
}

private fun day7part2Example(): Int {
    return readInputExample()
        .map { it.toRule() }
        .toMap()
        .contentsTotal("shiny gold bag")
}

private fun readInput(): List<String> {
    val file = File("src/main/resources/day_7_input")
    return file.readLines()
}

private fun readInputExample(): List<String> {
    val file = File("src/main/resources/day_7_input_example")
    return file.readLines()
}

private fun String.toRule(): Pair<String, List<Pair<Int, String>>> {
    val rule = split(" contain ")
    return rule[0].toSingularBag() to rule[1].toContents()
}

private fun String.toContents(): List<Pair<Int, String>> {
    if (this.trim() == "no other bags.") {
        return emptyList()
    }
    val contents = replace(".", "").split(", ")
    return contents.map { it.toQuantityBag() }
}

private fun String.toQuantityBag(): Pair<Int, String> {
    val match = Regex("(\\d*) (.*$)").find(this)
    val (quantity, bag) = match!!.destructured
    //println("quantity=$quantity bag=$bag")
    return quantity.toInt() to bag.toSingularBag()
}

private fun String.toSingularBag(): String {
    return trim().replace("bags", "bag")
}

private fun List<Pair<Int, String>>.canContain(bag: String): Boolean {
    return any { (quantity, ruleBag) -> quantity > 0 && bag == ruleBag }
}

//private fun Map<String, List<Pair<Int, String>>>.canContain(bag: String): Set<String> {
//    return canContain(
//        filter { (outerBag, contents) ->
//            contents.canContain(bag)
//        }.mapNotNull { (outerBag, _) -> outerBag }
//            .toSet()
//    )
//}
//
//private fun Map<String, List<Pair<Int, String>>>.canContain(bags: Set<String>): Set<String> {
//    println(bags)
//    return bags.map { this.canContain(it).flatten().toSet() }
//}

private fun Map<String, List<Pair<Int, String>>>.fullListOfBags(): Set<String> {
    return keys.plus(values.map { it.map { it.second } }.flatten()).toSet()
}

private fun Map<String, List<Pair<Int, String>>>.canContain(
    initialBag: String
): Set<String> {

    val toExplore = Stack<String>().apply { push(initialBag) }
    val explored = mutableSetOf<String>()
    val currentPossibilities = mutableSetOf<String>()

    while (toExplore.isNotEmpty()) {

        val bag = toExplore.pop()
        explored.add(bag)
        val allThatContain = allThatContain(bag)
        currentPossibilities.addAll(allThatContain)
        allThatContain.filter { !explored.contains(it) }
            .forEach { toExplore.push(it) }

    }
    return currentPossibilities

}

private fun Map<String, List<Pair<Int, String>>>.allThatContain(
    bag: String
): Set<String> {
    return filter { (outerBag, contents) -> contents.canContain(bag) }
        .map { (outerBag, _) -> outerBag }.toSet()
}

private fun Map<String, List<Pair<Int, String>>>.contentsTotal(bag: String): Int {
    val contents = get(bag)
    if (contents == null || contents.isEmpty()) return 0
    val result = contents.sumBy { (quantity, innerBag) -> quantity * (contentsTotal(innerBag) + 1) }
    println("bag=$bag result=$result")
    return result
}