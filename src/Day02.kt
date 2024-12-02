import kotlin.math.abs
import kotlin.math.sign

fun main() {

    fun checkReport(report: List<Int>): Boolean {
        val diff = report.zipWithNext { a, b -> a - b }
        val firstSign = diff.first().sign
        return diff.all { it.sign == firstSign && abs(it) in 1..3 }
    }

    fun part1(reports: List<List<Int>>): Int {
        return reports.count { report ->
            checkReport(report)
        }
    }

    fun generateSubReports(report: List<Int>): Sequence<List<Int>> =
        report.indices.asSequence().map { index ->
            report.take(index) + report.drop(index + 1)
        }

    fun part2(reports: List<List<Int>>): Int {
        return reports.count { report ->
            generateSubReports(report).any { subReport -> checkReport(subReport) }
        }
    }

    val testInput = readInputAsListOfInts("day2_test")
    check(part1(testInput) == 2)
    val partOneInput = readInputAsListOfInts("day2")
    part1(partOneInput).println()
    check(part2(testInput) == 4)
    part2(partOneInput).println()
}