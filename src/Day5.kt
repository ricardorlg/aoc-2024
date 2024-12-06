data class Page(val pageValue: Int, private val beforeList: List<Int>) : Comparable<Page> {
    override fun compareTo(other: Page): Int {
        if (other.pageValue in beforeList) return -1
        if (pageValue in other.beforeList) return 1
        return 0
    }

    override fun toString(): String {
        return pageValue.toString()
    }
}

data class Update(private val pages: List<Page>) {
    fun isSorted() = pages.zipWithNext().all { (a, b) -> a <= b }

    fun sorted() = Update(pages.sorted())

    fun midPageValue(): Int {
        return pages[pages.size / 2].pageValue
    }

    companion object {
        fun fromString(input: String, rules: Map<Int, List<Int>>): Update? {
            if (input.isBlank()) return null
            return Update(input.split(",").map { page ->
                val pageNumber = page.toInt()
                Page(pageNumber, rules[pageNumber].orEmpty())
            }
            )
        }
    }
}

fun main() {
    fun parseInput(input: List<String>): List<Update> {
        return input
            .partition { it.contains("|") }
            .let { (rulesData, updates) ->
                val rules = rulesData.groupBy({ it.split("|")[0].toInt() }, { it.split("|")[1].toInt() })
                updates.mapNotNull { Update.fromString(it, rules) }
            }
    }

    fun part1(input: List<String>): Long {
        val updates = parseInput(input)
        return updates.sumOfIf(Update::isSorted, Update::midPageValue)
    }

    fun part2(input: List<String>): Long {
        val updates = parseInput(input)
        return updates.sumOfIf({ !it.isSorted() }) { it.sorted().midPageValue() }
    }

    val testInput = readInput("day5_test")
    check(part1(testInput) == 143L)
    check(part2(testInput) == 123L)
    val input = readInput("day5")
    executeWithTime {
        part1(input)
    }
    executeWithTime(false) {
        part2(input)
    }
}