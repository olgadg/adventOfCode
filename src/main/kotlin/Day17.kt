import java.io.File

fun main(args: Array<String>) {
    try {
        //println(day17part1example())
        //println(day17part1())
        //println(day17part2example())
        println(day17part2())
    } catch (e: Exception) {
        println(e.printStackTrace())
    }
}

private fun day17part1(): String {
    return readInput()
            .cycle()
            .cycle()
            .cycle()
            .cycle()
            .cycle()
            .cycle()
            .countActive()
            .toString()
}

private fun day17part1example(): String {
    return readInput("src/main/resources/day_17_input_example")
            .cycle()
            .cycle()
            .cycle()
            .cycle()
            .cycle()
            .cycle()
            .countActive()
            .toString()
}


private fun day17part2(): String {
    return readInput()
            .toHyper()
            .cycleHyper()
            .cycleHyper()
            .cycleHyper()
            .cycleHyper()
            .cycleHyper()
            .cycleHyper()
            .countActiveHyper()
            .toString()
}

private fun day17part2example(): String {
    return readInput("src/main/resources/day_17_input_example")
            .toHyper()
            .cycleHyper()
            .cycleHyper()
            .cycleHyper()
            .cycleHyper()
            .cycleHyper()
            .cycleHyper()
            .countActiveHyper()
            .toString()
}

private fun readInput(fileName: String = "src/main/resources/day_17_input"): List<List<List<Char>>> {
    return listOf(File(fileName).readLines().map { it.toCharArray().toList() })
}

private fun List<List<List<Char>>>.toHyper(): List<List<List<List<Char>>>> {
    return listOf(this)
}


private fun List<List<List<Char>>>.countActive(): Int {
    return flatten().flatten().count { it == '#' }
}

private fun List<List<List<List<Char>>>>.countActiveHyper(): Int {
    return flatten().flatten().flatten().count { it == '#' }
}

private fun List<List<List<Char>>>.cycle(): List<List<List<Char>>> {
    return expand()
            .print()
            .applyRules()
            .print()
}


private fun List<List<List<List<Char>>>>.cycleHyper(): List<List<List<List<Char>>>> {
    return expandHyper()
            .applyRulesHyper()
}

private fun List<List<List<Char>>>.print(): List<List<List<Char>>> {
    return onEach {
        println()
        it.onEach { println(it.joinToString("")) }
    }.onEach { println("------") }
}

private fun List<List<List<Char>>>.getAdjacents(x: Int, y: Int, z: Int): List<Char> {
    return listOf(
            getOrNull(z - 1)?.getOrNull(y - 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(z - 1)?.getOrNull(y - 1)?.getOrNull(x) ?: '.',
            getOrNull(z - 1)?.getOrNull(y - 1)?.getOrNull(x + 1) ?: '.',
            getOrNull(z - 1)?.getOrNull(y)?.getOrNull(x - 1) ?: '.',
            getOrNull(z - 1)?.getOrNull(y)?.getOrNull(x) ?: '.',
            getOrNull(z - 1)?.getOrNull(y)?.getOrNull(x + 1) ?: '.',
            getOrNull(z - 1)?.getOrNull(y + 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(z - 1)?.getOrNull(y + 1)?.getOrNull(x) ?: '.',
            getOrNull(z - 1)?.getOrNull(y + 1)?.getOrNull(x + 1) ?: '.',
            getOrNull(z)?.getOrNull(y - 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(z)?.getOrNull(y - 1)?.getOrNull(x) ?: '.',
            getOrNull(z)?.getOrNull(y - 1)?.getOrNull(x + 1) ?: '.',
            getOrNull(z)?.getOrNull(y)?.getOrNull(x - 1) ?: '.',
            getOrNull(z)?.getOrNull(y)?.getOrNull(x + 1) ?: '.',
            getOrNull(z)?.getOrNull(y + 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(z)?.getOrNull(y + 1)?.getOrNull(x) ?: '.',
            getOrNull(z)?.getOrNull(y + 1)?.getOrNull(x + 1) ?: '.',
            getOrNull(z + 1)?.getOrNull(y - 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(z + 1)?.getOrNull(y - 1)?.getOrNull(x) ?: '.',
            getOrNull(z + 1)?.getOrNull(y - 1)?.getOrNull(x + 1) ?: '.',
            getOrNull(z + 1)?.getOrNull(y)?.getOrNull(x - 1) ?: '.',
            getOrNull(z + 1)?.getOrNull(y)?.getOrNull(x) ?: '.',
            getOrNull(z + 1)?.getOrNull(y)?.getOrNull(x + 1) ?: '.',
            getOrNull(z + 1)?.getOrNull(y + 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(z + 1)?.getOrNull(y + 1)?.getOrNull(x) ?: '.',
            getOrNull(z + 1)?.getOrNull(y + 1)?.getOrNull(x + 1) ?: '.'
    )
}

private fun List<List<List<Char>>>.applyRules(): List<List<List<Char>>> {
    return mapIndexed { z, plain ->
        plain.mapIndexed { y, row ->
            row.mapIndexed { x, cube ->
                val adjacents = getAdjacents(x, y, z)
                when (cube) {
                    '#' -> if (adjacents.shouldStayActive()) '#' else '.'
                    '.' -> if (adjacents.shouldActivate()) '#' else '.'
                    else -> error("wrong state $cube")
                }
            }
        }
    }
}

private fun List<Char>.shouldStayActive(): Boolean {
    return count { it == '#' } in 2..3
}

private fun List<Char>.shouldActivate(): Boolean {
    return count { it == '#' } == 3
}

private fun List<List<List<Char>>>.expand(): List<List<List<Char>>> {
    return (0..size + 1).map { z ->
        if (z == 0 || z == size + 1) {
            (0..this[0].size + 1).map { (0..this[0].size + 1).map { '.' } }
        } else {
            (0..this[0].size + 1).map { y ->
                if (y == 0 || y == this[0].size + 1) {
                    (0..this[0][0].size + 1).map { '.' }
                } else {
                    (0..this[0][0].size + 1).map { x ->
                        if (x == 0 || x == this[0][0].size + 1) '.' else this[z - 1][y - 1][x - 1]
                    }
                }
            }
        }
    }
}


private fun List<List<List<List<Char>>>>.getAdjacents(x: Int, y: Int, z: Int, w: Int): List<Char> {
    return listOf(
            getOrNull(w - 1)?.getOrNull(z - 1)?.getOrNull(y - 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(w - 1)?.getOrNull(z - 1)?.getOrNull(y - 1)?.getOrNull(x) ?: '.',
            getOrNull(w - 1)?.getOrNull(z - 1)?.getOrNull(y - 1)?.getOrNull(x + 1) ?: '.',
            getOrNull(w - 1)?.getOrNull(z - 1)?.getOrNull(y)?.getOrNull(x - 1) ?: '.',
            getOrNull(w - 1)?.getOrNull(z - 1)?.getOrNull(y)?.getOrNull(x) ?: '.',
            getOrNull(w - 1)?.getOrNull(z - 1)?.getOrNull(y)?.getOrNull(x + 1) ?: '.',
            getOrNull(w - 1)?.getOrNull(z - 1)?.getOrNull(y + 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(w - 1)?.getOrNull(z - 1)?.getOrNull(y + 1)?.getOrNull(x) ?: '.',
            getOrNull(w - 1)?.getOrNull(z - 1)?.getOrNull(y + 1)?.getOrNull(x + 1) ?: '.',
            getOrNull(w - 1)?.getOrNull(z)?.getOrNull(y - 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(w - 1)?.getOrNull(z)?.getOrNull(y - 1)?.getOrNull(x) ?: '.',
            getOrNull(w - 1)?.getOrNull(z)?.getOrNull(y - 1)?.getOrNull(x + 1) ?: '.',
            getOrNull(w - 1)?.getOrNull(z)?.getOrNull(y)?.getOrNull(x - 1) ?: '.',
            getOrNull(w - 1)?.getOrNull(z)?.getOrNull(y)?.getOrNull(x) ?: '.',
            getOrNull(w - 1)?.getOrNull(z)?.getOrNull(y)?.getOrNull(x + 1) ?: '.',
            getOrNull(w - 1)?.getOrNull(z)?.getOrNull(y + 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(w - 1)?.getOrNull(z)?.getOrNull(y + 1)?.getOrNull(x) ?: '.',
            getOrNull(w - 1)?.getOrNull(z)?.getOrNull(y + 1)?.getOrNull(x + 1) ?: '.',
            getOrNull(w - 1)?.getOrNull(z + 1)?.getOrNull(y - 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(w - 1)?.getOrNull(z + 1)?.getOrNull(y - 1)?.getOrNull(x) ?: '.',
            getOrNull(w - 1)?.getOrNull(z + 1)?.getOrNull(y - 1)?.getOrNull(x + 1) ?: '.',
            getOrNull(w - 1)?.getOrNull(z + 1)?.getOrNull(y)?.getOrNull(x - 1) ?: '.',
            getOrNull(w - 1)?.getOrNull(z + 1)?.getOrNull(y)?.getOrNull(x) ?: '.',
            getOrNull(w - 1)?.getOrNull(z + 1)?.getOrNull(y)?.getOrNull(x + 1) ?: '.',
            getOrNull(w - 1)?.getOrNull(z + 1)?.getOrNull(y + 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(w - 1)?.getOrNull(z + 1)?.getOrNull(y + 1)?.getOrNull(x) ?: '.',
            getOrNull(w - 1)?.getOrNull(z + 1)?.getOrNull(y + 1)?.getOrNull(x + 1) ?: '.',

            getOrNull(w)?.getOrNull(z - 1)?.getOrNull(y - 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(w)?.getOrNull(z - 1)?.getOrNull(y - 1)?.getOrNull(x) ?: '.',
            getOrNull(w)?.getOrNull(z - 1)?.getOrNull(y - 1)?.getOrNull(x + 1) ?: '.',
            getOrNull(w)?.getOrNull(z - 1)?.getOrNull(y)?.getOrNull(x - 1) ?: '.',
            getOrNull(w)?.getOrNull(z - 1)?.getOrNull(y)?.getOrNull(x) ?: '.',
            getOrNull(w)?.getOrNull(z - 1)?.getOrNull(y)?.getOrNull(x + 1) ?: '.',
            getOrNull(w)?.getOrNull(z - 1)?.getOrNull(y + 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(w)?.getOrNull(z - 1)?.getOrNull(y + 1)?.getOrNull(x) ?: '.',
            getOrNull(w)?.getOrNull(z - 1)?.getOrNull(y + 1)?.getOrNull(x + 1) ?: '.',
            getOrNull(w)?.getOrNull(z)?.getOrNull(y - 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(w)?.getOrNull(z)?.getOrNull(y - 1)?.getOrNull(x) ?: '.',
            getOrNull(w)?.getOrNull(z)?.getOrNull(y - 1)?.getOrNull(x + 1) ?: '.',
            getOrNull(w)?.getOrNull(z)?.getOrNull(y)?.getOrNull(x - 1) ?: '.',
            getOrNull(w)?.getOrNull(z)?.getOrNull(y)?.getOrNull(x + 1) ?: '.',
            getOrNull(w)?.getOrNull(z)?.getOrNull(y + 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(w)?.getOrNull(z)?.getOrNull(y + 1)?.getOrNull(x) ?: '.',
            getOrNull(w)?.getOrNull(z)?.getOrNull(y + 1)?.getOrNull(x + 1) ?: '.',
            getOrNull(w)?.getOrNull(z + 1)?.getOrNull(y - 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(w)?.getOrNull(z + 1)?.getOrNull(y - 1)?.getOrNull(x) ?: '.',
            getOrNull(w)?.getOrNull(z + 1)?.getOrNull(y - 1)?.getOrNull(x + 1) ?: '.',
            getOrNull(w)?.getOrNull(z + 1)?.getOrNull(y)?.getOrNull(x - 1) ?: '.',
            getOrNull(w)?.getOrNull(z + 1)?.getOrNull(y)?.getOrNull(x) ?: '.',
            getOrNull(w)?.getOrNull(z + 1)?.getOrNull(y)?.getOrNull(x + 1) ?: '.',
            getOrNull(w)?.getOrNull(z + 1)?.getOrNull(y + 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(w)?.getOrNull(z + 1)?.getOrNull(y + 1)?.getOrNull(x) ?: '.',
            getOrNull(w)?.getOrNull(z + 1)?.getOrNull(y + 1)?.getOrNull(x + 1) ?: '.',


            getOrNull(w + 1)?.getOrNull(z - 1)?.getOrNull(y - 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(w + 1)?.getOrNull(z - 1)?.getOrNull(y - 1)?.getOrNull(x) ?: '.',
            getOrNull(w + 1)?.getOrNull(z - 1)?.getOrNull(y - 1)?.getOrNull(x + 1) ?: '.',
            getOrNull(w + 1)?.getOrNull(z - 1)?.getOrNull(y)?.getOrNull(x - 1) ?: '.',
            getOrNull(w + 1)?.getOrNull(z - 1)?.getOrNull(y)?.getOrNull(x) ?: '.',
            getOrNull(w + 1)?.getOrNull(z - 1)?.getOrNull(y)?.getOrNull(x + 1) ?: '.',
            getOrNull(w + 1)?.getOrNull(z - 1)?.getOrNull(y + 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(w + 1)?.getOrNull(z - 1)?.getOrNull(y + 1)?.getOrNull(x) ?: '.',
            getOrNull(w + 1)?.getOrNull(z - 1)?.getOrNull(y + 1)?.getOrNull(x + 1) ?: '.',
            getOrNull(w + 1)?.getOrNull(z)?.getOrNull(y - 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(w + 1)?.getOrNull(z)?.getOrNull(y - 1)?.getOrNull(x) ?: '.',
            getOrNull(w + 1)?.getOrNull(z)?.getOrNull(y - 1)?.getOrNull(x + 1) ?: '.',
            getOrNull(w + 1)?.getOrNull(z)?.getOrNull(y)?.getOrNull(x - 1) ?: '.',
            getOrNull(w + 1)?.getOrNull(z)?.getOrNull(y)?.getOrNull(x) ?: '.',
            getOrNull(w + 1)?.getOrNull(z)?.getOrNull(y)?.getOrNull(x + 1) ?: '.',
            getOrNull(w + 1)?.getOrNull(z)?.getOrNull(y + 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(w + 1)?.getOrNull(z)?.getOrNull(y + 1)?.getOrNull(x) ?: '.',
            getOrNull(w + 1)?.getOrNull(z)?.getOrNull(y + 1)?.getOrNull(x + 1) ?: '.',
            getOrNull(w + 1)?.getOrNull(z + 1)?.getOrNull(y - 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(w + 1)?.getOrNull(z + 1)?.getOrNull(y - 1)?.getOrNull(x) ?: '.',
            getOrNull(w + 1)?.getOrNull(z + 1)?.getOrNull(y - 1)?.getOrNull(x + 1) ?: '.',
            getOrNull(w + 1)?.getOrNull(z + 1)?.getOrNull(y)?.getOrNull(x - 1) ?: '.',
            getOrNull(w + 1)?.getOrNull(z + 1)?.getOrNull(y)?.getOrNull(x) ?: '.',
            getOrNull(w + 1)?.getOrNull(z + 1)?.getOrNull(y)?.getOrNull(x + 1) ?: '.',
            getOrNull(w + 1)?.getOrNull(z + 1)?.getOrNull(y + 1)?.getOrNull(x - 1) ?: '.',
            getOrNull(w + 1)?.getOrNull(z + 1)?.getOrNull(y + 1)?.getOrNull(x) ?: '.',
            getOrNull(w + 1)?.getOrNull(z + 1)?.getOrNull(y + 1)?.getOrNull(x + 1) ?: '.'
    )
}

private fun List<List<List<List<Char>>>>.applyRulesHyper(): List<List<List<List<Char>>>> {
    return mapIndexed { w, hyper ->
        hyper.mapIndexed { z, plain ->
            plain.mapIndexed { y, row ->
                row.mapIndexed { x, cube ->
                    val adjacents = getAdjacents(x, y, z, w)
                    when (cube) {
                        '#' -> if (adjacents.shouldStayActive()) '#' else '.'
                        '.' -> if (adjacents.shouldActivate()) '#' else '.'
                        else -> error("wrong state $cube")
                    }
                }
            }
        }
    }
}

private fun List<List<List<List<Char>>>>.expandHyper(): List<List<List<List<Char>>>> {
    return (0..size + 1).map { w ->
        if (w == 0 || w == size + 1) {
            (0..this[0][0].size + 1).map { (0..this[0][0].size + 1).map { (0..this[0][0].size + 1).map { '.' } } }
        } else {
            (0..size + 1).map { z ->
                if (z == 0 || z == size + 1) {
                    (0..this[0][0].size + 1).map { (0..this[0][0].size + 1).map { '.' } }
                } else {
                    (0..this[0][0].size + 1).map { y ->
                        if (y == 0 || y == this[0][0].size + 1) {
                            (0..this[0][0][0].size + 1).map { '.' }
                        } else {
                            (0..this[0][0][0].size + 1).map { x ->
                                if (x == 0 || x == this[0][0][0].size + 1) '.' else this[w - 1][z - 1][y - 1][x - 1]
                            }
                        }
                    }
                }
            }
        }
    }
}
