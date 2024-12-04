import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/data/$name.txt").readText().trim().lines()

fun readInputAsListOfInts(name: String) = readInput(name).map {
    numberRegex.findAll(it).map { match -> match.value.toInt() }.toList()
}

fun readInputString(name: String) = Path("src/data/$name.txt").readText().trim()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

val numberRegex = "-?\\d+".toRegex()

fun String.equalsBySorted(other: String) = toCharArray().sorted() == other.toCharArray().sorted()

fun isPrime(n: Int) = "1".repeat(n).matches(""".?|(..+?)\1+""".toRegex()).not()
