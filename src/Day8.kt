fun main() {

    fun frequenciesMap(grid: Grid) = grid
        .points
        .filter { grid[it] != '.' }
        .groupBy { grid[it]!! }

    fun getAntiNodeLocation(
        antennasMap: Grid,
        antennaLocation: Point2D,
        offset: Point2D
    ): Point2D? {
        val antiNodeLocation = antennaLocation + offset
        return antiNodeLocation.takeIf { it in antennasMap }
    }

    fun allAntiNodesStartingAt(
        antennasMap: Grid,
        startingAt: Point2D,
        frequencies: Map<Char, List<Point2D>>,
        offset: Point2D
    ): Set<Point2D> {
        val antiNodes = mutableSetOf<Point2D>()
        var current = startingAt
        while (current in antennasMap) {
            val frequency = antennasMap[current]
            if (frequency == '.' || frequencies[frequency].orEmpty().size > 1) {
                antiNodes.add(current)
            }
            current += offset
        }
        return antiNodes
    }

    fun part1(input: List<String>): Int {
        val antennasMap = Grid(input)
        val frequenciesMap = frequenciesMap(antennasMap)
        val antiNodes = frequenciesMap
            .values
            .fold(mutableSetOf<Point2D>()) { antiNodes, locations ->
                locations
                    .allPairs()
                    .flatMapTo(antiNodes) { (p1, p2) ->
                        val offset = p2 - p1
                        listOfNotNull(
                            getAntiNodeLocation(antennasMap, p1, -offset),
                            getAntiNodeLocation(antennasMap, p2, offset)
                        )
                    }
            }
        return antiNodes.size
    }

    fun part2(input: List<String>): Int {
        val antennasMap = Grid(input)
        val frequenciesMap = frequenciesMap(antennasMap)
        val antiNodes = frequenciesMap
            .values
            .fold(mutableSetOf<Point2D>()) { acc, locations ->
                locations
                    .allPairs()
                    .flatMapTo(acc) { (p1, p2) -> allAntiNodesStartingAt(antennasMap, p1, frequenciesMap, p2 - p1) }
            }

        return antiNodes.size
    }

    val testInput = readInput("day8_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    val input = readInput("day8")
    executeWithTime {
        part1(input)
    }
    executeWithTime(false) {
        part2(input)
    }
}