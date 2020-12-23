import java.io.File
import kotlin.math.absoluteValue

fun main(args: Array<String>) {
    try {
        //println(day13part1example())
        //println(day13part1())
        println(day13part2example())
        println(day13part2())
    } catch (e: Exception) {
        println(e)
    }
}

private fun day13part1(): String {
    return readInput().part1().toString()
}


private fun day13part1example(): String {
    return readInput("src/main/resources/day_13_input_example")
            .part1()
            .toString()
}

private fun day13part2(): String {
    return readInput()
            .toBuses()
            .part2()
            .toString()
}

private fun day13part2example(): String {
    return readInput("src/main/resources/day_13_input_example")
            .toBuses()
            .part2()
            .toString()
}

private fun Pair<Int, List<String>>.part1(): Int {
    val departure = first
    val bus = second.toBuses()
            .map { getClosestTo(departure, it) }
            .minBy { it.second }!!
    println(bus)
    return bus.first * (bus.second - departure)

}

private fun readInput(fileName: String = "src/main/resources/day_13_input"): Pair<Int, List<String>> {
    val lines = File(fileName).readLines()
    return lines[0].toInt() to lines[1].split(",")
}

private fun List<String>.toBuses(): List<Int> {
    return filter { it != "x" }.map { it.toInt() }
}

private fun getClosestTo(departure: Int, bus: Int): Pair<Int, Int> {
    return bus to ((departure + bus) / bus) * bus
}

private fun Pair<Int, List<String>>.toBuses(): List<Bus> {
    return second
            .mapIndexed { index, bus ->
                if (bus != "x") {
                    val id = bus.toLong()
                    //Bus(id, (1 - index) % id)
                    Bus(id, index.toLong())
                    //Bus(id, index)
                } else null
            }
            .filterNotNull()
            .onEach { println(it) }
}

private fun List<Bus>.part2(): Long {
    val ids = map { it.id }
    val indexes = map { (it.id - it.minutes) % it.id }
    return chineseRemainder(ids, indexes)
}

//https://rosettacode.org/wiki/Chinese_remainder_theorem#Kotlin
// returns x where (a * x) % b == 1
fun multInv(a: Long, b: Long): Long {
    if (b == 1L) return 1
    var aa = a
    var bb = b
    var x0 = 0L
    var x1 = 1L
    while (aa > 1) {
        val q = aa / bb
        var t = bb
        bb = aa % bb
        aa = t
        t = x0
        x0 = x1 - q * x0
        x1 = t
    }
    if (x1 < 0) x1 += b
    return x1
}

fun chineseRemainder(n: List<Long>, a: List<Long>): Long {
    val prod = n.fold(1L) { acc, i -> acc * i }
    var sum = 0L
    for (i in 0 until n.size) {
        val p = prod / n[i]
        sum += a[i] * multInv(p, n[i]) * p
    }
    return sum % prod
}


data class Bus(val id: Long, val minutes: Long)
