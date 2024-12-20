package day19

import println
import readInput

enum class STRIPE(val colour: Char) {
    WHITE('w'),
    BLUE('u'),
    BLACK('b'),
    RED('r'),
    GREEN('g');

    companion object {
        fun fromChar(c: Char) = entries.first { it.colour == c }
    }
}

typealias Towel = List<STRIPE>

fun main() {
    fun part1(input: List<String>): Int {
        val towels: List<String> = input[0].split(',').sorted().map { it.trim() }
        val patterns = input.drop(2)

        val cache = mutableMapOf<String, Long>()
        fun solve(pattern: String): Long {
            return cache.getOrPut(pattern) {
                if (pattern.isEmpty()) {
                    1L
                } else {
                    towels.filter { pattern.startsWith(it) }.sumOf { t -> solve(pattern.removePrefix(t)) }
                }
            }
        }

        return patterns.count { solve(it) > 0 }
    }

    fun part2(input: List<String>): Long {
        val towels: List<String> = input[0].split(',').sorted().map { it.trim() }
        val patterns = input.drop(2)

        val cache = mutableMapOf<String, Long>()
        fun solve(pattern: String): Long {
            return cache.getOrPut(pattern) {
                if (pattern.isEmpty()) {
                    1L
                } else {
                    towels.filter { pattern.startsWith(it) }.sumOf { t -> solve(pattern.removePrefix(t)) }
                }
            }
        }

        return patterns.sumOf { solve(it) }
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("Day01_test_desc")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("day19/Day19_test")
    check(part1(testInput).println() == 6)
    check(part2(testInput).println() == 16L)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("day19/Day19")
    part1(input).println()
    part2(input).println()
}
