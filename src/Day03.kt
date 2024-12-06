fun main() {

    fun solve(input: String, regex: Regex): Long {
        return regex
            .findAll(input)
            .fold(true to 0L) { (enabled, result), it ->
                when (val operation = it.value) {
                    "do()" -> true to result
                    "don't()" -> false to result
                    else -> {
                        enabled to result + if (enabled) {
                            operation
                                .substring(4, operation.length - 1)
                                .split(",", limit = 2)
                                .map(String::toLong)
                                .reduce(Long::times)
                        } else 0
                    }
                }
            }.second
    }

    fun part1(input: String): Long {
        val regex = """mul\(\d{1,3},\d{1,3}\)""".toRegex()
        return solve(input, regex)
    }

    fun part2(input: String): Long {
        val regex = """do\(\)|don't\(\)|mul\(\d{1,3},\d{1,3}\)""".toRegex()
        return solve(input, regex)
    }

    val testInput = readInputString("day3_test")
    val partBTestInput = readInputString("day3b_test")
    check(part1(testInput) == 161L)
    check(part2(partBTestInput) == 48L)
    val input = readInputString("day3")
    executeWithTime {
        part1(input)
    }
    executeWithTime(false) {
        part2(input)
    }
}