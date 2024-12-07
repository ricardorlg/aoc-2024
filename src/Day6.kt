fun main() {

    fun parseInput(input: List<String>): Pair<Grid, Point2D> {
        val grid = Grid(input)
        for (r in input.indices) {
            for (c in input[r].indices) {
                if (input[r][c] == '^') {
                    return grid to Point2D(r, c)
                }
            }
        }
        error("No initial point found")
    }

    fun getPath(grid: Grid, initial: Point2D): Set<Point2D>? {
        val visited = mutableSetOf(initial to Direction.UP)
        val onlyPoints = mutableSetOf(initial)
        var current = initial
        var direction = Direction.UP
        while (true) {
            visited.add(current to direction)
            onlyPoints.add(current)
            val next = current.move(direction)
            if (grid[next] == null) {
                break
            }
            if (grid[next] == '#') {
                direction = direction.rotateToRight()
            } else {
                current = next
                if (visited.contains(current to direction)) {
                    return null
                }
                if (current != initial) {
                    grid[current] = 'X'
                }
            }
        }
        return onlyPoints
    }

    fun part1(input: List<String>): Int {
        val (grid, initial) = parseInput(input)
        val visited = getPath(grid, initial) ?: return 0
        return visited.size
    }


    fun part2(input: List<String>): Int {
        val (grid, initial) = parseInput(input)
        val safePath = getPath(grid, initial) ?: return 0
        return (safePath - initial).count { p ->
            grid[p] = '#'
            val path = getPath(grid, initial).also { grid[p] = '.' }
            path == null
        }
    }

    val testInput = readInput("day6_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)
    val input = readInput("day6")
    executeWithTime {
        part1(input)
    }
    executeWithTime(false) {
        part2(input)
    }

}