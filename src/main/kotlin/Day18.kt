import java.io.File

fun main(args: Array<String>) {
    try {
        //println(day18part1example())
        //println(day18part1())
        //println(day18part2example())
        println(day18part2())
    } catch (e: Exception) {
        println(e.printStackTrace())
    }
}

private fun day18part1(): String {
    return readInput()
            .map {
                //println(it.reverseOperation())
                val execute = it.reverseOperation().parse().execute()
                println(execute)
                execute
            }.sum()
            .toString()
}

private fun day18part1example(): String {
    return readInput("src/main/resources/day_18_input_example")
            .map {
                println(it.reverseOperation())
                it.reverseOperation().parse().execute()
            }
            .toString()
}


private fun day18part2example(): String {
    return readInput("src/main/resources/day_18_input_example")
            .map {
                println(it.reverseOperation())
                it.reverseOperation().parseWithPrecedence().execute()
            }
            .toString()
}


private fun day18part2(): String {
    return readInput()
            .map {
                println(it.reverseOperation())
                val execute = it.reverseOperation().parseWithPrecedence().execute()
                println(execute)
                execute
            }.sum()
            .toString()
}

private fun String.reverseOperation(): String {
    return reversed()
            .replace(')', '[')
            .replace('(', ')')
            .replace('[', '(')
}

private fun readInput(fileName: String = "src/main/resources/day_18_input"): List<String> {
    return File(fileName).readLines()
}

private fun String.parse(): MathExpression {
    val c = this[0]
    val (nextIndex, left) = when {
        c.isDigit() -> 1 to MathExpression.Atomic(c.toString().toLong())
        c == '(' -> {
            val endIndex = findClosingParenthesis()
            val partialExpression = substring(1, endIndex)
            endIndex + 1 to partialExpression.parse()
        }
        else -> error("something went wrong $c")
    }
    if (nextIndex == length) {
        return left
    }
    val operation = this[nextIndex + 1]
    return MathExpression.Operation(
            left,
            operation,
            substring(nextIndex + 3, length).parse()
    )
}

private fun String.parseWithPrecedence(): MathExpression {

    if (length == 1) {
        return MathExpression.Atomic(this[0].toString().toLong())
    }
    val openParenthesisIndex = indexOf('(')

    val multIndex = findOperation('*')
    if (multIndex != -1) {
        return MathExpression.Operation(
                substring(0, multIndex - 1).parseWithPrecedence(),
                '*',
                substring(multIndex + 2, length).parseWithPrecedence()
        )
    }

    val plusIndex = findOperation('+')
    if (plusIndex != -1) {
        return MathExpression.Operation(
                substring(0, plusIndex - 1).parseWithPrecedence(),
                '+',
                substring(plusIndex + 2, length).parseWithPrecedence()
        )
    }
    if (openParenthesisIndex != -1) {
        val closingParenthesisIndex = substring(openParenthesisIndex).findClosingParenthesis()
        return substring(openParenthesisIndex + 1, closingParenthesisIndex).parseWithPrecedence()
    }
    error("something went wrong")
}

private fun String.findOperation(symbol: Char): Int {
    var index = 0
    while (index < length) {
        val openParenthesisIndex = substring(index).indexOf('(')
        val indexOfSymbol = substring(index).indexOf(symbol)
        if (openParenthesisIndex == -1 || indexOfSymbol < openParenthesisIndex) {
            return if (indexOfSymbol == -1) -1 else index + indexOfSymbol
        } else {
            index += openParenthesisIndex + substring(index + openParenthesisIndex).findClosingParenthesis()
        }
    }
    return -1
}

private fun String.findClosingParenthesis(): Int {
    var openParenthesisCount = 0
    forEachIndexed { index, c ->
        when (c) {
            '(' -> openParenthesisCount++
            ')' -> if (openParenthesisCount == 1) return index else openParenthesisCount--
        }
    }
    error("Could not find closing parenthesis")
}

private fun MathExpression.execute(): Long {
    return when (this) {
        is MathExpression.Atomic -> value
        is MathExpression.Operation -> {
            val leftValue = left.execute()
            val rightValue = right.execute()
            when (symbol) {
                '+' -> leftValue + rightValue
                '*' -> leftValue * rightValue
                else -> error("could not found symbol $symbol")
            }
        }
    }
}


sealed class MathExpression {
    data class Atomic(val value: Long) : MathExpression()
    data class Operation(val left: MathExpression, val symbol: Char, val right: MathExpression) : MathExpression()
}