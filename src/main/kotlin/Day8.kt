import java.io.File

fun main(args: Array<String>) {
    try {
        //println(day8part1Example())
        //println(day8part1())
        println(day8part2())
    } catch (e: Exception) {
        println(e)
    }
}

private fun day8part1Example(): String {
    return readInputExample()
            .map { it.parseInstruction() }
            .executeUntilLoop()
            .toString()
}

private fun day8part1(): String {
    return readInput()
            .map { it.parseInstruction() }
            .executeUntilLoop()
            .toString()
}

private fun day8part2(): String {
    return readInput()
            .map { it.parseInstruction() }
            .changeInstructionUntilEnd()
            .toString()
}

private fun readInput(): List<String> {
    val file = File("src/main/resources/day_8_input")
    return file.readLines()
}

private fun readInputExample(): List<String> {
    val file = File("src/main/resources/day_8_input_example")
    return file.readLines()
}

private fun String.parseInstruction(): Instruction {
    val match = Regex("(jmp|acc|nop) ([-+])(\\d*)").find(this)
    val (instruction, sign, value) = match!!.destructured


    return Instruction(
            instruction,
            when (sign) {
                "+" -> value.toInt()
                "-" -> -value.toInt()
                else -> error("error Parsing")
            }
    )
}

private fun List<Instruction>.execute(acc: Int, position: Int): Pair<Int, Int> {
    val current = this[position]
    //println(current)
    return when (current.instruction) {
        "jmp" -> acc to position + current.value
        "acc" -> acc + current.value to position + 1
        else -> acc to position + 1
    }
}

private fun List<Instruction>.executeUntilLoop(): Int {
    var position = 0
    var acc = 0
    val executedPositions = mutableSetOf<Int>()

    while (!executedPositions.contains(position)) {
        executedPositions.add(position)
        val (resultAcc, resultPosition) = execute(acc, position)
        acc = resultAcc
        position = resultPosition
        //println("acc=$acc position=$position")
    }
    return acc
}

private fun List<Instruction>.executeUntilLoopOrFinalInstruction(): Int {
    var position = 0
    var acc = 0
    val executedPositions = mutableSetOf<Int>()

    while (!executedPositions.contains(position) && position < size - 1) {
        executedPositions.add(position)
        val (resultAcc, resultPosition) = execute(acc, position)
        acc = resultAcc
        position = resultPosition
    }
    if (position == size - 1) {
        return execute(acc, position).first
    }
    return Int.MAX_VALUE
}

private fun List<Instruction>.changeInstructionUntilEnd(): Int {
    var position = 0
    while (position <= size - 1) {
        val (newPosition, replacedList) = changeInstruction(position)
        val acc = replacedList.executeUntilLoopOrFinalInstruction()
        if (acc != Int.MAX_VALUE) {
            return acc
        }
        position = newPosition
    }
    error("Not found")
}

private fun List<Instruction>.changeInstruction(position: Int): Pair<Int, List<Instruction>> {
    val currentInstruction = get(position)
    val replacedList = toMutableList()
    replacedList[position] = currentInstruction.copyAndReplace()
    val nextPosition = nextNoopOrJmp(position+1)
    return nextPosition to replacedList

}

private fun List<Instruction>.nextNoopOrJmp(position: Int): Int {
    return (position..(size - 1)).find { get(it).let { it.isNoop || it.isJump } } ?: error("not found next instruction")
}


private data class Instruction(
        val instruction: String,
        val value: Int
) {
    val isNoop = instruction == "noop"
    val isJump = instruction == "jmp"

    fun copyAndReplace(): Instruction {
        return copy(instruction = if (isNoop) "jmp" else if (isJump) "noop" else instruction)
    }
}