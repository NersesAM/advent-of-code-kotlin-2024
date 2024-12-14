fun main() {
    data class Robot(val pos: Point, val velocity: Point)

    fun parseInput(input: List<String>): List<Robot> {
        val digits = """-?\d+""".toRegex()
        return input.map {
            val (x, y, vx, vy) = digits.findAll(it).map { it.value.toInt() }.toList()
            Robot(Point(x, y), Point(vx, vy))
        }
    }

    fun calcPositions(robots: List<Robot>, seconds: Int, gridSize: Point): List<Point> = robots.map {
        Point(
            (it.pos.x + it.velocity.x * seconds).mod(gridSize.x),
            (it.pos.y + it.velocity.y * seconds).mod(gridSize.y)
        )
    }

    fun printGrid(gridSize: Point, positions: List<Point>) {
        for (y in 0..<gridSize.y) {
            for (x in 0..<gridSize.x) {
                if (positions.any { it == Point(x, y) }) {
                    print(positions.count { it == Point(x, y) })
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

        return positions.count { pos -> pos.x < gridSize.x / 2 && pos.y < gridSize.y / 2 }.toLong() *
            positions.count { pos -> pos.x > gridSize.x / 2 && pos.y < gridSize.y / 2 } *
            positions.count { pos -> pos.x < gridSize.x / 2 && pos.y > gridSize.y / 2 } *
            positions.count { pos -> pos.x > gridSize.x / 2 && pos.y > gridSize.y / 2 }
    }

    fun part2(input: List<String>): Int {
        var i = 0
        w@ while (true) {
            val gridSize = Point(101, 103)
            val positions = calcPositions(parseInput(input), i, gridSize)

            for (p in positions) {
                val peak = buildList {
                    add(p)
                    repeat(4) {
                        add(Point(p.x - it, p.y + it))
                        add(Point(p.x + it, p.y + it))
                    }
                }
                if (peak.all(positions::contains)) {
                    printGrid(gridSize, positions)
                    break@w
                }
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

