import kotlin.math.abs

fun main() {
    fun parseInput(input: List<List<Int>>): Pair<List<Int>, List<Int>> {
        return input.map {
            val (left, right) = it
            left to right
        }.unzip()
    }

    fun part1(input: List<List<Int>>): Long {
        val (leftList, rightList) = parseInput(input)
        return leftList.sorted().zip(rightList.sorted()).sumOf { (l, r) ->
            abs(l - r).toLong()
        }
    }

    fun part2(input: List<List<Int>>): Long {
        val (leftList, rightList) = parseInput(input)
        val rightFreqMap = rightList.groupingBy { it }.eachCount()
        return leftList.sumOf {
            val freqOnRight = rightFreqMap[it] ?: 0
            it.toLong() * freqOnRight
        }
    }

    val testInput = readInputAsListOfInts("day1_test")
    check(part1(testInput) == 11L)
    check(part2(testInput) == 31L)
    val input = readInputAsListOfInts("day1")
    part1(input).println()
    part2(input).println()
}
