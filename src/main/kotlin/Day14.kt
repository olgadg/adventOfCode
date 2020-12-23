import java.io.File

fun main(args: Array<String>) {
    try {
        //println(day14part1example())
        //println(day14part1())
        println(day14part2example())
        println(day14part2())
    } catch (e: Exception) {
        println(e.printStackTrace())
    }
}

private fun day14part1(): String {
    return readInput()
            .part1()
            .toString()
}

private fun day14part1example(): String {
    return readInput("src/main/resources/day_14_input_example")
            .part1()
            .toString()
}

private fun day14part2(): String {
    return readInput()
            .part2()
            .toString()
}

private fun day14part2example(): String {
    return readInput("src/main/resources/day_14_input_example_2")
            .part2()
            .toString()
}

private fun List<String>.part1(): Long {
    return parseProgram()
            .map { (mask, programLines) ->
                programLines.map { programLine -> programLine.apply(mask) }
            }.flatten()
            .groupBy { it.address }
            .map { (_, values) -> values.map { it.longValue }.last() }
            .onEach { println(it) }
            .sum()
}



private fun List<String>.part2(): Long {
    return parseProgram()
            .map { (mask, programLines) ->
                programLines.map { programLine ->
                    programLine.applyFloating(mask)
                }.flatten()
            }.flatten()
            .groupBy { it.address }
            .map { (_, values) -> values.map { it.longValue }.last() }
            .onEach { println(it) }
            .sum()
}

private fun readInput(fileName: String = "src/main/resources/day_14_input"): List<String> {
    return File(fileName).readLines()
}

private fun List<String>.parseProgram(): List<Pair<Mask, List<ProgramLine>>> {
    val program = mutableListOf<Pair<Mask, MutableList<ProgramLine>>>()

    forEach { line ->
        if (line.startsWith("mask")) {
            program.add(line.toMask() to mutableListOf())
        } else {
            val programLines = program.last().second
            programLines.add(line.toProgramLine())
        }
    }

    return program

}

private fun String.toMask(): Mask {
    val match = Regex("mask = (.*)").find(this)
    val (mask) = match!!.destructured
    return mask
}

private fun String.toProgramLine(): ProgramLine {
    val match = Regex("mem\\[(\\d*)\\] = (\\d*)").find(this)
    val (address, value) = match!!.destructured
    return ProgramLine(address.toBinaryCharList(), value.toBinaryCharList())
}

private fun String.toBinaryCharList(): List<Char> {
    val charSequence: CharSequence = Integer.toBinaryString(this.toInt())
    val parsed = charSequence.padStart(36, '0')
    return parsed.toList()
}

private fun ProgramLine.apply(mask: Mask): ProgramLine {
    val applied = value.mapIndexed { index, c ->
        when (mask[index]) {
            'X' -> c
            '0' -> '0'
            '1' -> '1'
            else -> error("wrong mask value")
        }
    }
    return ProgramLine(address, applied)
}


private fun ProgramLine.applyFloating(mask: Mask): List<ProgramLine> {
    val appliedFloating = address.mapIndexed { index, c ->
        when (mask[index]) {
            'X' -> 'X'
            '0' -> c
            '1' -> '1'
            else -> error("wrong mask value")
        }
    }
    val programLines = mutableListOf<ProgramLine>()
    programLines.add(ProgramLine(appliedFloating, value))
    while (programLines.any { it.address.contains('X') }) {
        val programLine = programLines.find { it.address.contains('X') }!!
        programLines.remove(programLine)
        val indexOfX = programLine.address.indexOf('X')
        val programLine0 = programLine.replaceAddress(indexOfX, '0')
        programLines.add(programLine0)
        val programLine1 = programLine.replaceAddress(indexOfX, '1')
        programLines.add(programLine1)
        println("programLines=${programLines.size}")
    }
    return programLines
}

typealias Mask = String

//data class Mask(val value: String)
data class ProgramLine(val address: List<Char>, val value: List<Char>) {
    val addressLongValue : Long
        get() = value.joinToString("").toLong(2)
    val longValue: Long
        get() = value.joinToString("").toLong(2)

    override fun toString(): String {
        return "address=$address binary=${value.joinToString("")}"
    }

    fun replaceAddress(index: Int, char: Char): ProgramLine {
        val newValue = address.toMutableList()
        newValue[index] = char
        return copy(address = newValue)
    }
}


