package day16

import Point
import println
import readInput
import java.util.PriorityQueue

enum class DIR(val char: Char, val dx: Int, val dy: Int) {
    RIGHT('>', 1, 0),
    DOWN('v', 0, 1),
    LEFT('<', -1, 0),
    UP('^', 0, -1);

    fun turns() = when (this) {
        UP, DOWN -> listOf(LEFT, RIGHT)
        RIGHT, LEFT -> listOf(UP, DOWN)
    }
}

fun main() {

    data class Path(val p: Point, val dir: DIR, val parents: List<Path>, val distance: Int) {
        fun neighbours() = dir.turns().map { Path(Point(p.x + it.dx, p.y + it.dy), it, listOf(this), distance + 1001) } +
            Path(Point(p.x + dir.dx, p.y + dir.dy), dir, listOf(this), distance + 1)
    }

    fun printGrid(gridSize: Point, grid: Map<Point, Char>, settled: List<Path>) {
        for (y in 0..<gridSize.y) {
            for (x in 0..<gridSize.x) {
                print(settled.firstOrNull { it.p == Point(x, y) }?.dir?.char ?: grid[Point(x, y)] ?: '#')
            }
            println()
        }
        println()
    }

    fun part1(input: List<String>): Int {
        val grid = input.flatMapIndexed { y, line -> line.mapIndexed { x, char -> Point(x, y) to char } }
            .filterNot { it.second == '#' }.toMap()
        val start = grid.filterValues { it == 'S' }.keys.first()
        val end = grid.filterValues { it == 'E' }.keys.first()

        val settled = mutableListOf<Path>()
        val unsettled = PriorityQueue<Path>(compareBy { it.distance })
        unsettled.add(Path(start, DIR.RIGHT, listOf(), 0))

        while (unsettled.isNotEmpty()) {
            val current = unsettled.poll()
            val neighbours = current.neighbours()
                .filter { it.p in grid }
                .filterNot { it.p to it.dir in settled.map { it.p to it.dir } }

            for (n in neighbours) {
                val existing = unsettled.firstOrNull { it.p == n.p }
                if (existing != null && existing.distance > n.distance) {
                    unsettled.remove(existing)
                    unsettled.add(n)
                } else if (existing == null) {
                    unsettled.add(n)
                }
            }
            settled.add(current)
            if (current.p == end) break
        }
//        printGrid(Point(input[0].length, input.size), grid, settled)
        return settled.last().distance
    }

    fun part2(input: List<String>): Int {
        val grid = input.flatMapIndexed { y, line -> line.mapIndexed { x, char -> Point(x, y) to char } }
            .filterNot { it.second == '#' }.toMap()
        val start = grid.filterValues { it == 'S' }.keys.first()
        val end = grid.filterValues { it == 'E' }.keys.first()

        val settled = mutableListOf<Path>()
        val unsettled = PriorityQueue<Path>(compareBy { it.distance })
        unsettled.add(Path(start, DIR.RIGHT, listOf(), 0))

        while (unsettled.isNotEmpty()) {
            val current = unsettled.poll()
            val neighbours = current.neighbours()
                .filter { it.p in grid }
                .filterNot { it.p to it.dir in settled.map { it.p to it.dir } }

            for (n in neighbours) {
                val existing = unsettled.firstOrNull { it.p == n.p }
                if (existing != null && existing.distance > n.distance) {
                    unsettled.remove(existing)
                    unsettled.add(n)

                } else if (existing != null && existing.distance == n.distance) {
                    val e = Path(existing.p, existing.dir, existing.parents + n.parents, existing.distance)
                    unsettled.remove(existing)
                    unsettled.add(e)
                } else if (existing == null) {
                    unsettled.add(n)
                }
            }

            settled.add(current)
            if (current.p == end) break
        }
//        printGrid(Point(input[0].length, input.size), grid, settled)
        fun pathCount(parents: List<Path>): Set<Point> {
            if (parents.isEmpty()) return emptySet()
            return parents.map { it.p }.toSet() + parents.flatMap { pathCount(it.parents) }
        }

        return pathCount(listOf(settled.last())).size
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("Day01_test_desc")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("day16/Day16_test")
    check(part1(testInput).println() == 7036)
    check(part2(testInput).println() == 45)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("day16/Day16")
    part1(input).println()
//    partR(input).println()
    part2(input).println()
}
