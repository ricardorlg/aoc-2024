class Grid(private val data: List<String>) {
    private val cols = 0..data[0].lastIndex
    private val rows = 0..data.lastIndex
    val points = cols.flatMap { col -> rows.map { row -> Point2D(col, row) } }
    operator fun get(p: Point2D): Char = data.getOrNull(p.row)?.getOrNull(p.column) ?: '.'

    fun getDataOfSizeAndDirection(size: Int, p: Point2D, direction: Direction): String {
        return when (direction) {
            Direction.UP -> (0 until size).map { row -> this[Point2D(p.column, p.row - row)] }.joinToString("")
            Direction.DOWN -> (0 until size).map { row -> this[Point2D(p.column, p.row + row)] }.joinToString("")
            Direction.LEFT -> (0 until size).map { col -> this[Point2D(p.column - col, p.row)] }.joinToString("")
            Direction.RIGHT -> (0 until size).map { col -> this[Point2D(p.column + col, p.row)] }.joinToString("")
            Direction.UP_LEFT -> (0 until size).map { i -> this[Point2D(p.column - i, p.row - i)] }.joinToString("")
            Direction.UP_RIGHT -> (0 until size).map { i -> this[Point2D(p.column + i, p.row - i)] }.joinToString("")
            Direction.DOWN_LEFT -> (0 until size).map { i -> this[Point2D(p.column - i, p.row + i)] }.joinToString("")
            Direction.DOWN_RIGHT -> (0 until size).map { i -> this[Point2D(p.column + i, p.row + i)] }.joinToString("")
        }
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val wordToSearch = "XMAS"
        val grid = Grid(input)
        return grid.points.sumOf { p ->
            Direction.entries.count { dir ->
                grid.getDataOfSizeAndDirection(wordToSearch.length, p, dir) == wordToSearch
            }
        }
    }

    fun part2(input: List<String>): Int {
        val grid = Grid(input)
        return grid
            .points
            .count { p ->
                val current = grid[p]
                val left = grid[p.move(Direction.UP_LEFT)].toString() + grid[p.move(Direction.DOWN_RIGHT)]
                val right = grid[p.move(Direction.UP_RIGHT)].toString() + grid[p.move(Direction.DOWN_LEFT)]
                val isLeftValid = left == "MS" || left == "SM"
                val isRightValid = right == "MS" || right == "SM"
                current == 'A' && isLeftValid && isRightValid
            }
    }

    val testInput = readInput("day4_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)
    val partOneInput = readInput("day4")
    println(part1(partOneInput))
    println(part2(partOneInput))
}