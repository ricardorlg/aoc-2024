fun main() {

    fun getRegionsFrom(source: List<Point2D>, grid: Grid): MutableList<Set<Point2D>> {
        val regions = mutableListOf<Set<Point2D>>()
        val seen = mutableSetOf<Point2D>()
        source.forEach { point ->
            if (point !in seen) {
                seen += point
                val label = grid[point]!!
                val region = mutableSetOf<Point2D>(point)
                val stack = mutableListOf(point)
                while (stack.isNotEmpty()) {
                    val current = stack.removeFirst()
                    current.cardinalNeighbors().forEach { neighbor ->
                        if (grid[neighbor] == label && neighbor !in region) {
                            region.add(neighbor)
                            stack.add(neighbor)
                        }
                    }
                }
                seen += region
                regions.add(region)
            }
        }
        return regions
    }

    fun perimeter(region: Set<Point2D>): Set<Pair<Point2D, Direction>> {
        return region.flatMap { point ->
            point.cardinalNeighborsWithDirection()
                .filter { neighbor -> neighbor.first !in region }
        }.toSet()
    }

    fun sides(region: Set<Point2D>): Set<Pair<Point2D, Direction>> {
        val perimeter = perimeter(region)
        return perimeter.filter { (point, direction) ->
            val np = point.move(direction.rotateToRight())
            (np to direction) !in perimeter
        }.toSet()
    }

    fun part1(input: List<String>): Int {
        val grid = Grid(input)
        val points = grid.points
        val regions = getRegionsFrom(points, grid)
        return regions.sumOf { region ->
            val area = region.size
            val perimeter = perimeter(region).size
            area * perimeter
        }
    }

    fun part2(input: List<String>): Int {
        val grid = Grid(input)
        val points = grid.points
        val regions = getRegionsFrom(points, grid)
        return regions.sumOf { region ->
            val area = region.size
            val sides = sides(region).size
            area * sides
        }
    }

    val testInput = readInput("day12_test")
    check(part1(testInput) == 1930)
    check(part2(testInput) == 1206)

    val input = readInput("day12")
    executeWithTime {
        part1(input)
    }

    executeWithTime(false) {
        part2(input)
    }

}