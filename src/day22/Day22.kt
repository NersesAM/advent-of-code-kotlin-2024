package day22

import Point
import println
import readInput

fun main() {
    fun part1(input: List<String>): Long {
        val secrets = input.map { it.toLong() }

        return secrets.sumOf {
            var secret = it
            repeat(2000) {
                secret = ((secret shl 6) xor secret) and 16777215
                secret = ((secret shr 5) xor secret) and 16777215
                secret = ((secret shl 11) xor secret) and 16777215
            }
            secret
        }
    }

    fun part2(input: List<String>): Long {
        val secrets = input.map { it.toLong() }

        val x = secrets.map {
            var secret = it
            buildList {
                add(secret % 10)
                repeat(2000) {
                    secret = ((secret shl 6) xor secret) and 16777215
                    secret = ((secret shr 5) xor secret) and 16777215
                    secret = ((secret shl 11) xor secret) and 16777215
                    add(secret % 10)
                }
            }.zipWithNext { a, b -> b to b - a }
                .windowed(4) { it.map { it.second } to it.last().first }
                .distinctBy { it.first }.toMap()
        }

        val keys = x.map { it.keys }.flatten().distinct()
        return keys.maxOf { key -> x.sumOf { it.getOrDefault(key, 0L) } }
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("Day01_test_desc")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("day22/Day22_test")
    check(part1(testInput).println() == 37327623L)
    check(part2(testInput).println() == 24L)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("day22/Day22")
    part1(input).println()
    part2(input).println()
}
