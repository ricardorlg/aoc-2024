fun main() {

    fun part1(input: String): Long {
        val diskMap = input.flatMapIndexed { index, c ->
            val size = c.digitToInt()
            if (index.isEven()) {
                List(size) { index / 2 }
            } else {
                List(size) { -1 }
            }
        }.toMutableList()

        val freeSpace = diskMap.withIndex().filter { it.value == -1 }
        for ((i, _) in freeSpace) {
            while (diskMap.last() == -1) {
                diskMap.removeLast()
            }
            if (diskMap.size <= i) {
                break
            }
            diskMap[i] = diskMap.removeLast()
        }
        return diskMap.foldIndexed(0L) { index, acc, id -> acc + id * index }

    }

    fun part2(input: String): Long {
        val files = mutableMapOf<Int, Pair<Int, Int>>()
        val freeSpaces = mutableListOf<Pair<Int, Int>>()
        var startingAt = 0
        var fileId = 0

        input.forEachIndexed { index, c ->
            val size = c.digitToInt()
            if (index.isEven()) {
                files[fileId] = startingAt to size
                fileId += 1
            } else {
                if (size > 0) freeSpaces.add(startingAt to size)
            }
            startingAt += size
        }

        for (id in fileId - 1 downTo 0) {
            val (fileStart, fileSize) = files[id] ?: continue
            for ((index, freeSpace) in freeSpaces.withIndex()) {
                val (freeSpaceStart, freeSpaceSize) = freeSpace
                if (freeSpaceStart >= fileStart) {
                    freeSpaces.removeAllStartingAt(index)
                    break
                }
                if (fileSize <= freeSpaceSize) {
                    files[id] = freeSpaceStart to fileSize
                    if (fileSize == freeSpaceSize) {
                        freeSpaces.removeAt(index)
                    } else {
                        freeSpaces[index] = freeSpaceStart + fileSize to freeSpaceSize - fileSize
                    }
                    break
                }
            }
        }

        return files.entries.fold(0L) { acc, entry ->
            val (fileStart, size) = entry.value
            (fileStart until fileStart + size).fold(acc) { acc, i -> acc + entry.key * i }
        }
    }

    val testInput = readInputString("day9_test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    val input = readInputString("day9")
    executeWithTime {
        part1(input)
    }
    executeWithTime(false) {
        part2(input)
    }

}

private fun <T> MutableList<T>.removeAllStartingAt(start: Int) {
    (start until size).forEach { removeAt(start) }

}
