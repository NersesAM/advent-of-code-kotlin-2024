package day18

import Point
import println
import readInput
import java.util.PriorityQueue

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

    fun solve(corrupt: Set<Point>, gridSize: Int): Path? {
        val unsettled = PriorityQueue<Path>(compareBy { it.distance }).apply { offer(Path(Point(0, 0), 0)) }
        val settled = mutableListOf<Path>()

        while (unsettled.isNotEmpty()) {
            val current = unsettled.poll()
            for (n in current.neighbours()) {
                if (n.p in corrupt || n.p in settled.map { it.p } || n.p.x !in 0..gridSize || n.p.y !in 0..gridSize) continue
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
            if (current.p == Point(gridSize, gridSize)) break
        }


        return settled.firstOrNull { it.p == Point(gridSize, gridSize) }
    }

    fun part1(input: List<String>, gridSize: Int, corruptSize: Int): Int {
        val corrupt = input.map{ Point(it.split(',')[0].toInt(), it.split(',')[1].toInt()) }.take(corruptSize).toSet()
        return solve(corrupt, gridSize)!!.distance
    }

    fun part2(input: List<String>, gridSize: Int, corruptSize: Int): Point {
        val corrupt = input.map{ Point(it.split(',')[0].toInt(), it.split(',')[1].toInt()) }
        var a = corruptSize
        var b = corrupt.size

        while (b - 1 > a) {
            val m = (a + b)/2
            if (solve(corrupt.take((a + b)/2).toSet(), gridSize) == null) {
                b = m
            } else {
                a = m
            }
        }

        return corrupt[if (solve(corrupt.take(b).toSet(), gridSize) == null) a else b]
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("Day01_test_desc")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("day18/Day18_test")
    check(part1(testInput, 6, 12).println() == 22)
//    check(part2(testInput).println() == 1206)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("day18/Day18")
    part1(input, 70, 1024).println()
    part2(input, 70, 1024).println()
}
