fun main() {

    fun canMove(grid: Grid, fromPoint2D: Point2D, toPoint2D: Point2D): Boolean {
        val from = grid[fromPoint2D]?.digitToInt()
        val to = grid[toPoint2D]?.digitToInt()
        if (from == null || to == null) {
            return false
        }
        return from + 1 == to
    }

    fun solve(grid: Grid, start: Point2D, calculateRating: Boolean = false): Int {
        val queue = mutableListOf(start)
        //if calculateRating is true, we need to keep track of each time a height-9 point is visited
        //if calculateRating is false, we need to keep track of the number of unique height-9 points visited
        val heightNineStore = if (calculateRating) mutableListOf<Point2D>() else mutableSetOf<Point2D>()
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            val neighbors = current.cardinalNeighbors().filter { canMove(grid, current, it) }
            for (neighbor in neighbors) {
                val heightOfNeighbor = grid[neighbor]
                if (heightOfNeighbor == '9') {
                    heightNineStore.add(neighbor)
                } else {
                    queue.add(neighbor)
                }
            }
        }
        return heightNineStore.size
    }

    fun part1(input: List<String>): Int {
        val grid = Grid(input)
        val startingPoints = grid.points.filter { grid[it] == '0' }
        return startingPoints.sumOf { start ->
            solve(grid, start)
        }
    }

    fun part2(input: List<String>): Int {
        val grid = Grid(input)
        val startingPoints = grid.points.filter { grid[it] == '0' }
        return startingPoints.sumOf { start ->
            solve(grid, start, true)
        }
    }

    val testInput = readInput("day10_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = readInput("day10")
    executeWithTime {
        part1(input)
    }
    executeWithTime(false) {
        part2(input)
    }

}
