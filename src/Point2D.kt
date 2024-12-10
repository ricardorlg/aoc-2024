import kotlin.math.abs

data class Point2D(
    val row: Int,
    val column: Int,
) {

    operator fun plus(other: Point2D): Point2D {
        return Point2D(row + other.row, column + other.column)
    }

    operator fun minus(other: Point2D): Point2D {
        return Point2D(row - other.row, column - other.column)
    }

    operator fun unaryMinus(): Point2D {
        return Point2D(-row, -column)
    }

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

    fun cardinalNeighbors(): List<Point2D> {
        return listOf(
            move(Direction.UP),
            move(Direction.DOWN),
            move(Direction.LEFT),
            move(Direction.RIGHT),
        )
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
