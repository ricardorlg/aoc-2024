enum class Operation {
    ADD, MULTIPLY, CONCATENATE;

    fun calculate(a: Long, b: Long, inverse: Boolean = false): Long {
        return if (inverse) {
            when (this) {
                ADD -> a - b
                MULTIPLY -> a / b
                CONCATENATE -> a.toString().dropLast(b.toString().length).toLong()
            }
        } else {
            when (this) {
                ADD -> a + b
                MULTIPLY -> a * b
                CONCATENATE -> "$a$b".toLong()
            }
        }
    }

    companion object {
        fun withoutConcatenate() = entries.filter { it != CONCATENATE }
    }
}


fun main() {

    fun parse(input: String): Pair<Long, List<Long>> {
        return input.split(":", limit = 2)
            .let {
                it[0].toLong() to it[1].toLongList()
            }
    }

    fun checkIfPossible(target: Long, equation: List<Long>, operations: List<Operation>): Boolean {
        if (equation.size == 1) return equation[0] == target
        return operations.any { operation ->
            val lastValue = equation.last()
            val canBeCalculated = when (operation) {
                Operation.ADD -> target > lastValue
                Operation.MULTIPLY -> target % lastValue == 0L
                Operation.CONCATENATE -> target.toString().hasSuffix(lastValue.toString())
            }
            if (canBeCalculated) {
                checkIfPossible(operation.calculate(target, lastValue, true), equation.dropLast(1), operations)
            } else {
                false
            }
        }
    }

    fun solve(input: List<String>, operations: List<Operation>): Long {
        return input.fold(0L) { acc, line ->
            val (target, equation) = parse(line)
            val valid = checkIfPossible(target, equation, operations)
            if (valid) acc + target else acc
        }
    }

    fun part1(input: List<String>) = solve(input, Operation.withoutConcatenate())

    fun part2(input: List<String>) = solve(input, Operation.entries)


    val testInput = readInput("day7_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    val input = readInput("day7")

    executeWithTime {
        part1(input)
    }

    executeWithTime(false) {
        part2(input)
    }
}