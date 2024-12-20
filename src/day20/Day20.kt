package day20

import Point
import println
import readInput
import java.util.PriorityQueue
import kotlin.math.abs

fun main() {
    data class Path(val p: Point, val distance: Int) {
        fun neighbours(): List<Path> {
            return listOf(
                Path(Point(p.x + 1, p.y), distance + 1),
                Path(Point(p.x - 1, p.y), distance + 1),
                Path(Point(p.x, p.y + 1), distance + 1),
                Path(Point(p.x, p.y - 1), distance + 1)
            )
        }
    }

    fun solve(input: List<String>, maxCheat: Int): Int {
        val grid = input.flatMapIndexed { y, line -> line.mapIndexed { x, char -> Point(x, y) to char } }.toMap()
        val start = grid.filterValues { it == 'S' }.keys.first()
        val walls = grid.filterValues { it == '#' }.keys

        val unsettled = PriorityQueue<Path>(compareBy { it.distance }).apply { offer(Path(start, 0)) }
        val settled = mutableListOf<Path>()

        while (unsettled.isNotEmpty()) {
            val current = unsettled.poll()
            for (n in current.neighbours()) {
                if (n.p in walls || n.p in settled.map { it.p }) continue
                val existing = unsettled.firstOrNull { it.p == n.p }
                if (existing != null) {
                    if (n.distance < existing.distance) {
                        unsettled.remove(existing)
                        unsettled.offer(n)
                    }
                } else {
                    unsettled.offer(n)
                }
            }
            settled.add(current)
        }

        var count = mutableMapOf<Int, Int>()

        val settledPairs = settled.flatMap { s ->
            val (point, dist) = s
            settled.filter {
                val cartesian = abs(it.p.x - point.x) + abs(it.p.y - point.y)
                cartesian <= maxCheat && it.distance - dist > cartesian
            }.map { it to s }
        }

        for ((a, b) in settledPairs) {
            val cheatpicos = a.distance - b.distance - (abs(a.p.x - b.p.x) + abs(a.p.y - b.p.y))
            count[cheatpicos] = count.getOrDefault(cheatpicos, 0) + 1
        }

        return count.entries.sumOf { if (it.key >= 100) it.value else 0 }
    }

    fun part1(input: List<String>): Int {
        return solve(input, 2)
    }


    fun part2(input: List<String>): Int {
        return solve(input, 20)
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("Day01_test_desc")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("day20/Day20_test")
//    check(part1(testInput).println() == 1930)
//    check(part2(testInput).println() == 1206)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("day20/Day20")
    part1(input).println()
    part2(input).println()
}
