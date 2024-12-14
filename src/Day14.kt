fun main() {
    data class Robot(val pos: Point, val velocity:Point)
    fun parseInput(input: List<String>): List<Robot> {
        val digits = """-?\d+""".toRegex()
        return input.map {
            val (x, y, vx, vy) = digits.findAll(it).map { it.value.toInt() }.toList()
            Robot(Point(x, y), Point(vx, vy))
        }
    }

    fun calcPositions(robots: List<Robot>, seconds: Int, gridSize: Point): List<Robot> = robots.map {
        Robot(
            Point(
                (it.pos.x + it.velocity.x * seconds).mod(gridSize.x),
                (it.pos.y + it.velocity.y * seconds).mod(gridSize.y)
            ), it.velocity
        )
    }

    fun printGrid(gridSize: Point, positions: List<Robot>) {
        for (y in 0..<gridSize.y) {
            for (x in 0..<gridSize.x) {
                if (positions.any { it.pos == Point(x, y) }) {
                    print(positions.count { it.pos == Point(x, y) })
                } else {
                    print(".")
                }
            }
            println()
        }
    }

    fun part1(input: List<String>, gridSize: Point, seconds: Int = 100): Long {
        val robots = parseInput(input).println()

        val positions = calcPositions(robots, seconds, gridSize)

        printGrid(gridSize, positions)

        return positions.count { (pos, _) -> pos.x in 0 ..< gridSize.x/2 && pos.y in 0 ..< gridSize.y/2 }.toLong() *
            positions.count { (pos, _) -> pos.x in gridSize.x/2 + 1 ..< gridSize.x && pos.y in 0 ..< gridSize.y/2 } *
            positions.count { (pos, _) -> pos.x in 0 ..< gridSize.x/2 && pos.y in gridSize.y/2 + 1 ..< gridSize.y } *
            positions.count { (pos, _) -> pos.x in gridSize.x/2 + 1 ..< gridSize.x && pos.y in gridSize.y/2 + 1 ..< gridSize.y }
    }

    fun part2(input: List<String>): Int {
        var i = 0
        while (true) {
            val gridSize = Point(101, 103)
            val positions = calcPositions(parseInput(input), i, gridSize)

            val middleColumn = positions.filter { it.pos.x == gridSize.x/2 }.map { it.pos.y }.toSortedSet()
            var maxconsecutive = 0
            var currentconsecutive = 0
            for (y in 0..<gridSize.y) {
                if (middleColumn.contains(y)) {
                    currentconsecutive++
                } else {
                    if (currentconsecutive > maxconsecutive) {
                        maxconsecutive = currentconsecutive
                    }
                    currentconsecutive = 0
                }
            }
            if (maxconsecutive>10) {
                printGrid(gridSize, positions)
                break
            }
            i++
        }

        return i
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("Day01_test_desc")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day14_test")
    check(part1(testInput, Point(11, 7)).println() == 12L)
//    check(part2(testInput).println() == 1206L)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day14")
    part1(input, Point(101, 103)).println()
    part2(input).println()
}

