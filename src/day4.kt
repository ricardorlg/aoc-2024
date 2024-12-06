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
    val input = readInput("day4")
    executeWithTime {
        part1(input)
    }
    executeWithTime(false) {
        part2(input)
    }
}