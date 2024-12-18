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

    operator fun times(scalar: Int): Point2D {
        return Point2D(row * scalar, column * scalar)
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

    fun cardinalNeighborsWithDirection(): List<Pair<Point2D, Direction>> {
        return listOf(
            move(Direction.UP) to Direction.UP,
            move(Direction.DOWN) to Direction.DOWN,
            move(Direction.LEFT) to Direction.LEFT,
            move(Direction.RIGHT) to Direction.RIGHT,
        )
    }

    fun directionTo(other: Point2D): Direction {
        return when {
            row < other.row && column == other.column -> Direction.DOWN
            row > other.row && column == other.column -> Direction.UP
            row == other.row && column < other.column -> Direction.RIGHT
            row == other.row && column > other.column -> Direction.LEFT
            row < other.row && column < other.column -> Direction.DOWN_RIGHT
            row < other.row -> Direction.DOWN_LEFT
            row > other.row && column < other.column -> Direction.UP_RIGHT
            row > other.row -> Direction.UP_LEFT
            else -> throw IllegalArgumentException("Points are the same")
        }
    }

    fun fixedAtSize(size: Pair<Int, Int>): Point2D {
        return Point2D(row.mod(size.first), column.mod(size.second))
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
