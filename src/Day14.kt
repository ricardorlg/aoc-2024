data class Robot(
    val position: Point2D,
    private val velocity: Point2D,
    private val spaceSize: Pair<Int, Int>
) {
    fun quadrant(): Int {
        val height = spaceSize.first
        val width = spaceSize.second
        //check if robot is exactly at middle if so return 0
        if (position.row == height / 2 || position.column == width / 2) return 0
        return when {
            position.row < height / 2 && position.column < width / 2 -> 1
            position.row < height / 2 && position.column >= width / 2 -> 2
            position.row >= height / 2 && position.column < width / 2 -> 3
            else -> 4
        }
    }

    fun move(times: Int): Robot {
        val newPosition = (velocity * times) + position
        return Robot(newPosition.fixedAtSize(spaceSize), velocity, spaceSize)
    }
}

fun main() {

    fun getRobots(input: List<String>, demo: Boolean = false): List<Robot> {
        val spaceSize = if (demo) Pair(7, 11) else Pair(103, 101)
        return input.map { l ->
            val (robotData, velocityData) = l.split(" ", limit = 2).map { it.toIntList() }
            Robot(Point2D(robotData[1], robotData[0]), Point2D(velocityData[1], velocityData[0]), spaceSize)
        }
    }

    fun calculateSafetyFactor(robots: List<Robot>, seconds: Int): Int {
        val robotsPerQuadrant = robots.groupingBy { it.move(seconds).quadrant() }.eachCount().filterKeys { it != 0 }
        return robotsPerQuadrant.values.reduce { acc, i -> acc * i }
    }

    fun part1(input: List<String>, demo: Boolean = false): Int {
        val robots = getRobots(input, demo)
        return calculateSafetyFactor(robots, 100)
    }

    fun part2(input: List<String>): Int {
        val height = 103
        val width = 101
        val timeRange = (1..(height * width))
        val robots = getRobots(input)
        return timeRange.map { calculateSafetyFactor(robots, it) to it }.minBy { it.first }.second

    }

    val testInput = readInput("day14_test")
    check(part1(testInput, true) == 12)

    val input = readInput("day14")
    executeWithTime {
        part1(input)
    }

    executeWithTime(false) {
        part2(input)
    }

}