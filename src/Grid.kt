class Grid(input: List<String>) {
    private val grid = input.map { it.toCharArray() }

    val points = grid.indices.flatMap { r -> grid[r].indices.map { c -> Point2D(r, c) } }

    operator fun get(p: Point2D): Char? {
        return grid.getOrNull(p.row)?.getOrNull(p.column)
    }

    operator fun set(p: Point2D, value: Char) {
        grid[p.row][p.column] = value
    }

    fun getDataOfSizeAndDirection(size: Int, p: Point2D, direction: Direction): String {
        return when (direction) {
            Direction.UP -> (0 until size).mapNotNull { row -> this[Point2D(p.row - row, p.column)] }
            Direction.DOWN -> (0 until size).mapNotNull { row -> this[Point2D(p.row + row, p.column)] }
            Direction.LEFT -> (0 until size).mapNotNull { col -> this[Point2D(p.row, p.column - col)] }
            Direction.RIGHT -> (0 until size).mapNotNull { col -> this[Point2D(p.row, p.column + col)] }
            Direction.UP_LEFT -> (0 until size).mapNotNull { i -> this[Point2D(p.row - i, p.column - i)] }
            Direction.UP_RIGHT -> (0 until size).mapNotNull { i -> this[Point2D(p.row - i, p.column + i)] }
            Direction.DOWN_LEFT -> (0 until size).mapNotNull { i -> this[Point2D(p.row + i, p.column - i)] }
            Direction.DOWN_RIGHT -> (0 until size).mapNotNull { i -> this[Point2D(p.row + i, p.column + i)] }
        }.joinToString("")
    }

    override fun toString(): String {
        return grid.joinToString("\n") { it.joinToString("") }
    }
}