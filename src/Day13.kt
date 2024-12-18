class Machine(
    private val buttonA: Button,
    private val buttonB: Button,
    private val prize: Prize,
    private val withLimit: Boolean
) {
    data class Button(val cost: Int, val moveRight: Long, val moveForward: Long)
    data class Prize(val x: Long, val y: Long)

    fun prizeCost(): Long {
        val (buttonAPushes, buttonBPushes) = solveSystemOfTwoEquations(
            a1 = buttonA.moveRight,
            b1 = buttonB.moveRight,
            c1 = prize.x,
            a2 = buttonA.moveForward,
            b2 = buttonB.moveForward,
            c2 = prize.y
        )
        if (withLimit && (buttonAPushes > 100 || buttonBPushes > 100)) return 0
        if (buttonAPushes % 1 != 0.0 || buttonBPushes % 1 != 0.0) return 0
        return (buttonAPushes * buttonA.cost + buttonBPushes * buttonB.cost).toLong()
    }
}

fun main() {

    fun parseInput(input: String, withLimit: Boolean): List<Machine> {
        val movePrize = if (withLimit) 0 else 10000000000000
        val buttonACost = 3
        val buttonBCost = 1
        return input
            .split("\n\n")
            .map { data ->
                val (a, b, p) = data.split("\n", limit = 3).map { it.toLongList() }
                val buttonA = Machine.Button(buttonACost, a[0], a[1])
                val buttonB = Machine.Button(buttonBCost, b[0], b[1])
                val prize = Machine.Prize(p[0] + movePrize, p[1] + movePrize)
                Machine(buttonA, buttonB, prize, withLimit)
            }
    }

    fun part1(input: String): Long {
        val machines = parseInput(input, true)
        return machines.sumOf(Machine::prizeCost)
    }

    fun part2(input: String): Long {
        val machines = parseInput(input, false)
        return machines.sumOf(Machine::prizeCost)
    }


    val testInput = readInputString("day13_test")
    check(part1(testInput) == 480L)

    val input = readInputString("day13")
    executeWithTime {
        part1(input)
    }
    executeWithTime(false) {
        part2(input)
    }


}