sealed interface Stone {
    companion object {
        fun fromValue(value: Long) = if ("$value".length.isEven()) {
            EvenStone("$value")
        } else {
            NormalStone(value)
        }
    }

    data class EvenStone(val value: String) : Stone {
        fun mutate(): Pair<Stone, Stone> {
            val left = value.substring(0, value.toString().length / 2).trimStart('0').ifEmpty { "0" }
            val right = value.substring(value.toString().length / 2).trimStart('0').ifEmpty { "0" }
            return fromValue(left.toLong()) to fromValue(right.toLong())
        }
    }

    data class NormalStone(val value: Long) : Stone {
        fun mutate() = if (value == 0L) {
            NormalStone(1)
        } else {
            fromValue(value * 2024)
        }
    }

}

fun main() {

    fun blink(stone: Stone, times: Int, memory: MutableMap<Pair<Stone, Int>, Long>): Long {
        if (times == 0) return 1
        return when (stone) {
            is Stone.EvenStone -> {
                val (leftStone, rightStone) = stone.mutate()
                memory.getOrPut(leftStone to times - 1) { blink(leftStone, times - 1, memory) } +
                        memory.getOrPut(rightStone to times - 1) { blink(rightStone, times - 1, memory) }
            }

            is Stone.NormalStone -> {
                val newStone = stone.mutate()
                memory.getOrPut(newStone to times - 1) { blink(newStone, times - 1, memory) }
            }
        }
    }

    fun solve(input: String, times: Int): Long {
        val initialArrangement = input.toLongList().map(Stone::fromValue)
        val memory = mutableMapOf<Pair<Stone, Int>, Long>()
        return initialArrangement.sumOf { blink(it, times, memory) }
    }

    fun part1(input: String): Long {
        return solve(input, 25)
    }

    fun part2(input: String): Long {
        return solve(input, 75)
    }

    val testInput = readInputString("day11_test")
    check(part1(testInput) == 55312L)

    val input = readInputString("day11")
    executeWithTime {
        part1(input)
    }
    executeWithTime(false) {
        part2(input)
    }
}