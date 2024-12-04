data class Point2D(
    val column: Int,
    val row: Int
){
    fun move(direction: Direction): Point2D {
        return when (direction) {
            Direction.UP -> Point2D(column, row - 1)
            Direction.DOWN -> Point2D(column, row + 1)
            Direction.LEFT -> Point2D(column - 1, row)
            Direction.RIGHT -> Point2D(column + 1, row)
            Direction.UP_LEFT -> Point2D(column - 1, row - 1)
            Direction.UP_RIGHT -> Point2D(column + 1, row - 1)
            Direction.DOWN_LEFT -> Point2D(column - 1, row + 1)
            Direction.DOWN_RIGHT -> Point2D(column + 1, row + 1)
        }
    }
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
}
