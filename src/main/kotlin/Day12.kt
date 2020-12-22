import java.io.File

fun main(args: Array<String>) {
    try {
        //println(day12part1example())
        //println(day12part1())
        //println(day12part2example())
        println(day12part2())
    } catch (e: Exception) {
        println(e)
    }
}

private fun day12part1(): String {
    return readInput()
            .fold(PositionDirection(), { state, move ->
                println(state)
                state.executeMove(move)
            }).let { Math.abs(it.x) + Math.abs(it.y) }.toString()
}

private fun day12part1example(): String {
    return readInput("src/main/resources/day_12_input_example")
            .fold(PositionDirection(), { state, move ->
                println(state)
                state.executeMove(move)
            })
            .let { Math.abs(it.x) + Math.abs(it.y) }
            .toString()
}


private fun day12part2(): String {
    return readInput()
            .fold(Situation(Position(10, 1), Position(0, 0)), { situation, move ->
                val newSituation = situation.executeMove(move)
                println("move=$move situation=$newSituation")
                newSituation
            })
            .let { Math.abs(it.ship.x) + Math.abs(it.ship.y) }
            .toString()
}

private fun day12part2example(): String {
    return readInput("src/main/resources/day_12_input_example")
            .fold(Situation(Position(10, 1), Position(0, 0)), { situation, move ->
                val newSituation = situation.executeMove(move)
                println("move=$move situation=$newSituation")
                newSituation
            })
            .let { Math.abs(it.ship.x) + Math.abs(it.ship.y) }
            .toString()
}

private fun readInput(fileName: String = "src/main/resources/day_12_input"): List<Move> {
    return File(fileName).readLines().map { it.toMove() }
}

private fun String.toMove(): Move {
    val match = Regex("(\\w)(\\d*)").find(this)
    val (instruction, value) = match!!.destructured

    return Move(instruction[0], value.toInt())
}

private fun PositionDirection.executeMove(move: Move): PositionDirection {
    return when (move.instruction) {
        'F' -> moveOnDirection(direction, move.value, direction)
        'R' -> PositionDirection(x, y, rotateRight(move.value))
        'L' -> PositionDirection(x, y, rotateLeft(move.value))
        'N', 'E', 'S', 'W' -> moveOnDirection(move.instruction, move.value, direction)
        else -> error("Move unknown $move")
    }
}

private fun PositionDirection.moveOnDirection(direction: Char, value: Int, positionDirection: Char): PositionDirection {
    return when (direction) {
        'N' -> PositionDirection(x, y + value, positionDirection)
        'E' -> PositionDirection(x + value, y, positionDirection)
        'S' -> PositionDirection(x, y - value, positionDirection)
        'W' -> PositionDirection(x - value, y, positionDirection)
        else -> error("Unknown direction $direction")
    }
}

private fun PositionDirection.rotateRight(value: Int): Char {
    val currentIndex = DIRECTIONS.indexOf(direction)
    val index = (currentIndex + value / 90).rem(4)
    return DIRECTIONS[index]
}

private fun PositionDirection.rotateLeft(value: Int): Char {
    val currentIndex = DIRECTIONS.indexOf(direction)
    val index = (4 + currentIndex - value / 90).rem(4)
    return DIRECTIONS[index]
}

private fun Situation.executeMove(move: Move): Situation {
    return when (move.instruction) {
        'R', 'L' -> Situation(waypoint.rotate(move), ship)
        'F' -> Situation(waypoint, ship.moveOnDirection(move.value, waypoint))
        'N' -> Situation(Position(waypoint.x, waypoint.y + move.value), ship)
        'E' -> Situation(Position(waypoint.x + move.value, waypoint.y), ship)
        'S' -> Situation(Position(waypoint.x, waypoint.y - move.value), ship)
        'W' -> Situation(Position(waypoint.x - move.value, waypoint.y), ship)
        else -> error("Unknown instruction $move.instruction")
    }
}

private fun Position.moveOnDirection(value: Int, waypoint: Position): Position {
    return Position(x + value * waypoint.x, y + value * waypoint.y)
}


private fun Position.rotate(move: Move): Position {
    return when {
        move.value == 90 && move.instruction == 'R' || move.value == 270 && move.instruction == 'L' ->
            Position(y, -x)
        move.value == 180 -> Position(-x, -y)
        move.value == 270 && move.instruction == 'R' || move.value == 90 && move.instruction == 'L' ->
            Position(-y, x)
        else -> error("Unknown move $move")
    }
}

private data class Move(val instruction: Char, val value: Int)

private data class PositionDirection(val x: Int = 0, val y: Int = 0, val direction: Char = 'E')

private val DIRECTIONS = arrayOf('N', 'E', 'S', 'W')

private data class Position(val x: Int = 0, val y: Int = 0)

private data class Situation(val waypoint: Position, val ship: Position)