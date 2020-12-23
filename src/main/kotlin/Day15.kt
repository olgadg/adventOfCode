import java.io.File

fun main(args: Array<String>) {
    try {
        println(day15part1example())
        println(day15part1())

        //println(day15part2example())
        println(day15part2())
    } catch (e: Exception) {
        println(e.printStackTrace())
    }
}

private fun day15part1(): String {
    return readInput().find(2020).toString()
}

private fun day15part1example(): String {
    return readInput("src/main/resources/day_15_input_example")
            .find(2020)
            .toString()
}


private fun day15part2(): String {
    return readInput().find(30000000).toString()
}

private fun day15part2example(): String {
    return readInput("src/main/resources/day_15_input_example")
            .find(30000000)
            .toString()
}

private fun readInput(fileName: String = "src/main/resources/day_15_input"): List<Int> {
    return File(fileName).readLines()[0].split(",").map { it.toInt() }
}

private fun Numbers.nextNumber(): Int {
    val (index, previousIndex) = getLastIndexes()
    val next = when {
        previousIndex == -1 -> 0
        else -> {
            index - previousIndex
        }
    }
    add(next)
    return next
}

private fun List<Int>.find(number: Int): Int {
    val numbers = Numbers(this)

    return (size until number).map {
        println(it)
        numbers.nextNumber()
    }.last()
}

data class NumberWithIndex(
        val number: Int,
        val index: Int,
        val previousIndex: Int = -1
)


data class Indexes(
        val index: Int,
        val previousIndex: Int = -1
)


class Numbers(numbers: List<Int>) {

    private val map: HashMap<Int, Indexes> = hashMapOf()
    private val list: MutableList<Int> = arrayListOf()
    init {
        numbers.forEachIndexed { index, number ->
            map.put(number, Indexes(index))
            list.add(number)
        }

    }

    fun add(number: Int) {
        map.put(number, Indexes(list.size, map[number]?.index ?: -1))
        list.add(number)
    }

    fun getLastIndexes() = map[list.last()]!!
}