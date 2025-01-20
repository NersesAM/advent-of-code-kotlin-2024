package day25

import Point
import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val (l, k) = input.filter{ it.isNotEmpty() }.windowed(7, 7).partition { it[0] == "#####" }
        val locks = l.map {
            it.drop(1).dropLast(1).fold(Array(5) { 0 }) { acc, key ->
                key.forEachIndexed { i, c -> acc[i] = acc[i] + if (c == '#') 1 else 0 }
                acc
            }
        }
        val keys = k.map {
            it.drop(1).dropLast(1).fold(Array(5) { 0 }) { acc, key ->
                key.forEachIndexed { i, c -> acc[i] = acc[i] + if (c == '#') 1 else 0 }
                acc
            }
        }
        var count = 0
        for (key in keys) {
            loop@ for (lock in locks) {
                for (i in 0..4) {
                    if (key[i] + lock[i] > 5) continue@loop
                }
                count++
            }
        }

        return count
    }

    fun part2(input: List<String>): Int {
        val grid = input.flatMapIndexed { y, line -> line.mapIndexed { x, char -> Point(x, y) to char } }.toMap()

        return grid.size
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("Day01_test_desc")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("day25/Day25_test")
    check(part1(testInput).println() == 3)
//    check(part2(testInput).println() == 1206)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("day25/Day25")
    part1(input).println()
    part2(input).println()
}
