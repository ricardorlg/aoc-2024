class Box(val position: Point2D) {
    fun gpsCoordinate(): Int {
        return 100 * position.row + position.column
    }
}


fun main() {

    fun Char.toDirection(): Direction {
        return when (this) {
            '^' -> Direction.UP
            '>' -> Direction.RIGHT
            '<' -> Direction.LEFT
            'v' -> Direction.DOWN
            else -> error("Unknown")
        }
    }

    fun parseInput(input: String): Pair<Grid, List<String>> {
        val (gridData, movementsData) = input.split("\n\n", limit = 2)
        val grid = Grid(gridData.lines())
        val movements = movementsData.lines()
        return grid to movements
    }

    fun parseAndDuplicateGrid(input: String): Pair<Grid, List<String>> {
        val (gridData, movementsData) = input.split("\n\n", limit = 2)
        val movements = movementsData.lines()
        val updatedData = buildString {
            gridData.lines().forEachIndexed { i, line ->
                line.forEach { c ->
                    if (c == '#') {
                        append("##")
                    }
                    if (c == '.') {
                        append("..")
                    }
                    if (c == 'O') {
                        append("[]")
                    }
                    if (c == '@') {
                        append("@.")
                    }
                }
                if (i != gridData.lines().lastIndex) {
                    append("\n")
                }
            }
        }
        val grid = Grid(updatedData.lines())
        return grid to movements
    }

    fun canMoveSimpleBox(point: Point2D, grid: Grid, direction: Direction): Boolean {
        val pointsToCheck = when (direction) {
            Direction.UP -> {
                val row = point.row
                val column = point.column
                (row downTo 1).map { Point2D(it, column) }
            }

            Direction.DOWN -> {
                val row = point.row
                val column = point.column
                (row until grid.rows).map { Point2D(it, column) }
            }

            Direction.LEFT -> {
                val row = point.row
                val column = point.column
                (column downTo 1).map { Point2D(row, it) }
            }

            Direction.RIGHT -> {
                val row = point.row
                val column = point.column
                (column until grid.columns).map { Point2D(row, it) }
            }

            else -> error("$direction no supported")
        }
        var changed = false
        for (p in pointsToCheck) {
            if (p == point) {
                continue
            }
            if (grid[p] == 'O') {
                continue
            }
            if (grid[p] == '#') {
                break
            }
            if (grid[p] == '.') {
                grid[p] = 'O'
                grid[point] = '.'
                changed = true
                break
            }
        }
        return changed
    }

    fun moveHorizontally(point: Point2D, grid: Grid, direction: Direction): Boolean {
        val row = point.row
        val column = point.column
        val pointsToCheck = when (direction) {
            Direction.LEFT -> (column downTo 0).map { Point2D(row, it) }
            Direction.RIGHT -> (column until grid.columns).map { Point2D(row, it) }
            else -> error("Not supported")
        }
        val firstEmptySpace = pointsToCheck.firstOrNull { grid[it] == '.' }
        if (firstEmptySpace == null) {
            return false
        }
        val firstWall = pointsToCheck.first { grid[it] == '#' }
        if (direction == Direction.LEFT && firstWall.column > firstEmptySpace.column) {
            return false
        }
        if (direction == Direction.RIGHT && firstWall.column < firstEmptySpace.column) {
            return false
        }
        if (direction == Direction.LEFT) {
            (firstEmptySpace.column until point.column)
                .forEach { c ->
                    grid[Point2D(row, c)] = grid[Point2D(row, c + 1)]!!
                    grid[Point2D(row, c + 1)] = '.'
                }
        } else {
            (firstEmptySpace.column downTo point.column + 1)
                .forEach { c ->
                    grid[Point2D(row, c)] = grid[Point2D(row, c - 1)]!!
                    grid[Point2D(row, c - 1)] = '.'
                }
        }

        return grid[point] == '.'
    }

    fun canMoveComplexBox(point: Point2D, grid: Grid, direction: Direction): Boolean {
        if (direction == Direction.LEFT || direction == Direction.RIGHT) {
            return moveHorizontally(point, grid, direction)
        }
        if (direction == Direction.UP) {
            val row = point.row
            val edge = 1
            var target = edge
            val charAtPoint = grid[point]!!
            val otherPoint = when (charAtPoint) {
                '[' -> point.move(Direction.RIGHT)
                ']' -> point.move(Direction.LEFT)
                else -> error("Unknown")
            }
            val pointsToMove = mutableMapOf(row to mutableListOf(point, otherPoint))
            for (r in row - 1 downTo edge + 1) {
                pointsToMove.computeIfAbsent(r) {
                    mutableListOf()
                }
                val previousRowMoves = pointsToMove[r + 1]
                    .orEmpty()
                    .toSet()
                    .sortedBy { it.column }
                    .chunked(2)

                if (previousRowMoves.isEmpty()) {
                    target = r
                    break
                }
                previousRowMoves.forEach { pair ->
                    val (left, right) = pair
                    val leftUp = left.move(Direction.UP)
                    val rightUp = right.move(Direction.UP)
                    val atLUP = grid[leftUp]
                    val atRUP = grid[rightUp]
                    if (atLUP == '[') {
                        pointsToMove[r]!!.add(leftUp)
                    }
                    if (atLUP == ']') {
                        val data = listOf(leftUp.move(Direction.LEFT), leftUp)
                        pointsToMove[r]!!.addAll(data)
                    }
                    if (atRUP == '[') {
                        val data = listOf(rightUp, rightUp.move(Direction.RIGHT))
                        pointsToMove[r]!!.addAll(data)
                    }
                    if (atRUP == ']') {
                        pointsToMove[r]!!.add(rightUp)
                    }
                }
            }
            for (r in target until row) {
                val points = pointsToMove[r + 1]
                points
                    .orEmpty()
                    .sortedBy { it.column }
                    .chunked(2)
                    .forEach { pair ->
                        val (left, right) = pair
                        val cl = left.move(Direction.UP)
                        val cr = right.move(Direction.UP)
                        if (grid[cl] == '.' && grid[cr] == '.') {
                            grid[cl] = grid[left]!!
                            grid[cr] = grid[right]!!
                            grid[left] = '.'
                            grid[right] = '.'
                        }
                    }
            }
        } else {
            val row = point.row
            val edge = grid.rows - 1
            var target = edge
            val charAtPoint = grid[point]!!
            val otherPoint = when (charAtPoint) {
                '[' -> point.move(Direction.RIGHT)
                ']' -> point.move(Direction.LEFT)
                else -> error("Unknown")
            }
            val pointsToMove = mutableMapOf(row to mutableListOf(point, otherPoint))
            for (r in row + 1..edge) {
                pointsToMove.computeIfAbsent(r) {
                    mutableListOf()
                }
                val previousRowMoves = pointsToMove[r - 1]
                    .orEmpty()
                    .toSet()
                    .sortedBy { it.column }
                    .chunked(2)

                previousRowMoves.forEach { pair ->
                    val (left, right) = pair
                    val leftDown = left.move(Direction.DOWN)
                    val rightDown = right.move(Direction.DOWN)
                    val atLD = grid[leftDown]
                    val atRUD = grid[rightDown]
                    if (atLD == '[') {
                        pointsToMove[r]!!.add(leftDown)
                    }
                    if (atLD == ']') {
                        val data = listOf(leftDown.move(Direction.LEFT), leftDown)
                        pointsToMove[r]!!.addAll(data)
                    }
                    if (atRUD == '[') {
                        val data = listOf(rightDown, rightDown.move(Direction.RIGHT))
                        pointsToMove[r]!!.addAll(data)
                    }
                    if (atRUD == ']') {
                        pointsToMove[r]!!.add(rightDown)
                    }
                }
            }
            for (r in target downTo row - 1) {
                val points = pointsToMove[r - 1]
                points
                    .orEmpty()
                    .sortedBy { it.column }
                    .chunked(2)
                    .forEach { pair ->
                        val (left, right) = pair
                        val cl = left.move(Direction.DOWN)
                        val cr = right.move(Direction.DOWN)
                        if (grid[cl] == '.' && grid[cr] == '.') {
                            grid[cl] = grid[left]!!
                            grid[cr] = grid[right]!!
                            grid[left] = '.'
                            grid[right] = '.'
                        }
                    }
            }
        }
        return grid[point] == '.'
    }

    fun part1(input: String): Int {
        val (grid, movements) = parseInput(input)
        val robotStartingPoint = grid.points.find { grid[it] == '@' } ?: error("robot not found")
        var currentPosition = robotStartingPoint
        movements.flatMap { it.toList() }
            .forEach { mov ->
                val direction = mov.toDirection()
                val next = currentPosition.move(direction)
                val atNext = grid[next]
                if (atNext != '#') {
                    if (atNext == '.') {
                        grid[currentPosition] = '.'
                        currentPosition = next
                        grid[currentPosition] = '@'
                    }
                    if (atNext == 'O') {
                        if (canMoveSimpleBox(next, grid, direction)) {
                            grid[currentPosition] = '.'
                            currentPosition = next
                            grid[currentPosition] = '@'
                        }
                    }
                }

            }
        val boxes = grid.points.filter { grid[it] == 'O' }.map { Box(it) }
        return boxes.sumOf { it.gpsCoordinate() }
    }

    fun part2(input: String): Int {
        val (grid, movements) = parseAndDuplicateGrid(input)
        val robotStartingPoint = grid.points.find { grid[it] == '@' } ?: error("robot not found")
        var currentPosition = robotStartingPoint
        movements
            .flatMap { it.toList() }
            .forEach { mov ->
                val direction = mov.toDirection()
                val next = currentPosition.move(direction)
                val atNext = grid[next]!!
                val isComplexBox = atNext == '[' || atNext == ']'
                if (atNext != '#') {
                    if (atNext == '.') {
                        grid[currentPosition] = '.'
                        grid[next] = '@'
                        currentPosition = next
                    }
                    if (isComplexBox) {
                        val gridBefore = grid.clone()
                        if (canMoveComplexBox(next, grid, direction)) {
                            grid[currentPosition] = '.'
                            grid[next] = '@'
                            currentPosition = next
                        } else {
                            grid.updateGrid(gridBefore)
                        }
                    }
                }
            }

        val boxes = grid.points.filter { grid[it] == '[' }
            .map { Box(it) }

        return boxes.sumOf { it.gpsCoordinate() }
    }

    val testInput = readInputString("day15_test")
//    check(part1(testInput) == 10092) { "Test failed" }
    println(part2(testInput))

    val input = readInputString("day15")
    executeWithTime {
        part1(input)
    }
    executeWithTime(true) {
        part2(input)
    }

}
