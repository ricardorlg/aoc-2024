import kotlin.math.abs

data class Point2D(
    val row: Int,
    val column: Int,
) {
    fun move(direction: Direction): Point2D {
        return when (direction) {
            Direction.UP -> Point2D(row = row - 1, column = column)
            Direction.DOWN -> Point2D(row = row + 1, column = column)
            Direction.LEFT -> Point2D(row = row, column = column - 1)
            Direction.RIGHT -> Point2D(row = row, column = column + 1)
            Direction.UP_LEFT -> Point2D(row = row - 1, column = column - 1)
            Direction.UP_RIGHT -> Point2D(row = row - 1, column = column + 1)
            Direction.DOWN_LEFT -> Point2D(row = row + 1, column = column - 1)
            Direction.DOWN_RIGHT -> Point2D(row = row + 1, column = column + 1)
        }
    }

    fun distanceTo(other: Point2D): Int {
        return abs(column - other.column) + abs(row - other.row)
    }
}

enum class Direction {
    UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT;

    fun rotateToRight(): Direction {
        return when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
            UP_LEFT -> UP_RIGHT
            UP_RIGHT -> DOWN_RIGHT
            DOWN_RIGHT -> DOWN_LEFT
            DOWN_LEFT -> UP_LEFT
        }
    }
}
