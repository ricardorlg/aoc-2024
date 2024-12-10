import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.time.measureTimedValue

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/data/$name.txt").readText().trim().lines()

fun readInputAsListOfInts(name: String) = readInput(name).map {
    it.toIntList()
}

fun String.toIntList() = numberRegex.findAll(this).map { match -> match.value.toInt() }.toList()

fun String.toLongList() = numberRegex.findAll(this).map { match -> match.value.toLong() }.toList()

fun readInputString(name: String) = Path("src/data/$name.txt").readText().trim()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

val numberRegex = "-?\\d+".toRegex()

fun String.equalsBySorted(other: String) = toCharArray().sorted() == other.toCharArray().sorted()

fun isPrime(n: Int) = "1".repeat(n).matches(""".?|(..+?)\1+""".toRegex()).not()

inline fun <T> Iterable<T>.sumOfIf(predicate: (T) -> Boolean, selector: (T) -> Int): Long {
    var sum = 0L
    for (element in this) {
        if (predicate(element)) {
            sum += selector(element)
        }
    }
    return sum
}

fun List<String>.toGrid(): Map<Point2D, Char> {
    val grid = mutableMapOf<Point2D, Char>()
    forEachIndexed { row, line ->
        line.forEachIndexed { col, c ->
            grid[Point2D(col, row)] = c
        }
    }
    return grid
}

inline fun executeWithTime(part1: Boolean = true, block: () -> Any) {
    val (result, duration) = measureTimedValue(block)
    if (part1) {
        println("Part 1")
    } else {
        println("Part 2")
    }
    println("--------------------")
    println("Execution time: $duration")
    println("Result: $result")
    println("--------------------")
}

fun <T> combinationsWithRepetition(source: List<T>, size: Int): List<List<T>> {
    if (size == 0) return listOf(emptyList())
    val result = mutableListOf<List<T>>()
    for (element in source) {
        for (combination in combinationsWithRepetition(source, size - 1)) {
            result.add(listOf(element) + combination)
        }
    }
    return result
}

fun <T> combinationsWithoutRepetition(source: List<T>, size: Int): List<List<T>> {
    if (size == 0) return listOf(emptyList())
    val result = mutableListOf<List<T>>()
    for (i in source.indices) {
        val element = source[i]
        val rest = source.subList(i + 1, source.size)
        for (combination in combinationsWithoutRepetition(rest, size - 1)) {
            result.add(listOf(element) + combination)
        }
    }
    return result
}

fun <T> List<T>.allPairs(): List<List<T>> {
    val result = mutableListOf<List<T>>()
    for (i in indices) {
        for (j in indices) {
            if (i == j) continue
            result.add(listOf(this[i], this[j]))
        }
    }
    return result
}

fun String.hasSuffix(other: String, strict: Boolean = true): Boolean {
    return if (strict) {
        this.length > other.length && this.endsWith(other)
    } else {
        this.endsWith(other)
    }
}

fun Int.isEven() = this % 2 == 0
fun Int.isOdd() = this % 2 != 0

fun <T> listOfSizeAndValue(size: Int, value: T): List<T>? {
    return if (size > 0) {
        List(size) { value }
    } else {
        null
    }
}