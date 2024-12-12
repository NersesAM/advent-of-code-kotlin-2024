fun main() {
    fun Point.neighbours() = listOf(Point(x - 1, y), Point(x + 1, y), Point(x, y - 1), Point(x, y + 1))

    class Region {

        val points = mutableSetOf<Point>()
        fun add(p: Point) = points.add(p)
        fun contains(p: Point) = points.contains(p)
        fun area() = points.size
        fun perimeter() = 4 * points.size - points.sumOf { it.neighbours().filter(this::contains).size }
        fun sides(grid: Map<Point, Char>): Int {
            val left = points.minOf { it.x }
            val right = points.maxOf { it.x }
            val top = points.minOf { it.y }
            val bottom = points.maxOf { it.y }
            val food = grid[points.first()]!!

            var count = 0
            var n: Pair<Boolean?, Boolean?> = Pair(null, null)

            for (x in left..right + 1) {
                for (y in top..bottom) {
                    val l = grid[Point(x - 1, y)]
                    val r = grid[Point(x, y)]
                    if (l == food && points.contains(Point(x - 1, y)) || r == food && points.contains(Point(x, y))) {
                        if (l != r && (l == food) to (r == food) != n) {
                            count++
                        }
                    }
                    n = Pair(l == food, r == food)
                }
                n = Pair(null, null)
            }

            n = Pair(null, null)
            for (y in top..bottom + 1) {
                for (x in left..right) {
                    val t = grid[Point(x, y - 1)]
                    val b = grid[Point(x, y)]
                    if (t == food && points.contains(Point(x, y - 1)) || b == food && points.contains(Point(x, y))) {
                        if (t != b && (t == food) to (b == food) != n) {
                            count++
                        }
                    }
                    n = Pair(t == food, b == food)
                }
                n = Pair(null, null)
            }

            return count
        }
    }


    fun findRegions(grid: Map<Point, Char>): List<Region> {
        val regions = mutableListOf<Region>()

        fun findRegion(start: Point, veg: Char, r: Region) {
            if (r.contains(start)) return
            if (grid[start] != veg) return
            r.add(start)
            for (neighbour in start.neighbours()) {
                findRegion(neighbour, veg, r)
            }
        }

        val visited = mutableSetOf<Point>()

        do {
            val start = grid.keys.first { it !in visited }
            val region = Region()
            findRegion(start, grid[start]!!, region)
            visited.addAll(region.points)
            regions.add(region)
        } while (grid.keys.any { it !in visited })

        return regions
    }

    fun part1(input: List<String>): Int {
        val grid = input.flatMapIndexed { y, line -> line.mapIndexed { x, char -> Point(x, y) to char } }.toMap()
        val regions = findRegions(grid)

        return regions.sumOf { it.area() * it.perimeter() }
    }

    fun part2(input: List<String>): Int {
        val grid = input.flatMapIndexed { y, line -> line.mapIndexed { x, char -> Point(x, y) to char } }.toMap()
        val regions = findRegions(grid)

        return regions.sumOf { it.area() * it.sides(grid) }
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("Day01_test_desc")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day12_test")
    check(part1(testInput).println() == 1930)
    check(part2(testInput).println() == 1206)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
