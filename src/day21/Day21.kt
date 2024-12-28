package day21

import Point
import println
import readInput
import java.lang.ProcessBuilder.Redirect.to
import kotlin.math.abs
import kotlin.math.min

typealias Button = Char

fun main() {
    val UP = '^'
    val RIGHT = '>'
    val DOWN = 'v'
    val LEFT = '<'
    val ACTIVATE = 'A'
    /*
    --- Numerical Keypad ---
        +---+---+---+
        | 7 | 8 | 9 |
        +---+---+---+
        | 4 | 5 | 6 |
        +---+---+---+
        | 1 | 2 | 3 |
        +---+---+---+
            | 0 | A |
            +---+---+
     */
    val numpad = mapOf(
        '7' to Point(0, 0),
        '8' to Point(1, 0),
        '9' to Point(2, 0),
        '4' to Point(0, 1),
        '5' to Point(1, 1),
        '6' to Point(2, 1),
        '1' to Point(0, 2),
        '2' to Point(1, 2),
        '3' to Point(2, 2),
        '0' to Point(1, 3),
        'A' to Point(2, 3),
    )

    fun numpadMoves(from: Button, to: Button): List<Button> {
        val fromPos = numpad.getValue(from)
        val toPos = numpad.getValue(to)

        val xdiff = fromPos.x - toPos.x
        val ydiff = fromPos.y - toPos.y

        val ret = mutableListOf<Button>()
        fun addx() {
            repeat(abs(xdiff)) { ret.add(if (xdiff >= 0) LEFT else RIGHT) }
        }
        if (ydiff > 0) {
            if (fromPos.y == 3 && toPos.x == 0) {
                repeat(ydiff) { ret.add(UP) }
                addx()
            } else {
                addx()
                repeat(ydiff) { ret.add(UP) }
            }
        } else {
            repeat(-ydiff) { ret.add(DOWN) }
            addx()
        }
        return ret + ACTIVATE
    }

    /*
    --- Directional Keypad ---
            +---+---+
            | ^ | A |
        +---+---+---+
        | < | v | > |
        +---+---+---+
     */
    val dirpad = mapOf(
        '^' to Point(1, 0),
        'A' to Point(2, 0),
        '<' to Point(0, 1),
        'v' to Point(1, 1),
        '>' to Point(2, 1),
    )

    data class Key(val code: List<Button>, val depth: Int)
    val cache = mutableMapOf<Key, Long>()
    fun dirpadMoves(code: List<Button>, depth: Int): Long {
        return cache.getOrPut(Key(code, depth)) {
            if (depth == 0) return code.size.toLong()

            var fromPos = dirpad.getValue('A')
            var size = 0L
            for (button in code) {
                val toPos = dirpad.getValue(button)

                val xdiff = fromPos.x - toPos.x
                val ydiff = fromPos.y - toPos.y

                val xmoves = mutableListOf<Button>()
                val ymoves = mutableListOf<Button>()
                repeat(abs(xdiff)) { xmoves.add(if (xdiff > 0) LEFT else RIGHT) }
                repeat(abs(ydiff)) { ymoves.add(if (ydiff > 0) UP else DOWN) }

                size += min(
                    if (fromPos.y == 0 && toPos.x == 0) Long.MAX_VALUE else dirpadMoves(xmoves + ymoves + ACTIVATE, depth - 1),
                    if (fromPos.x == 0 && toPos.y == 0) Long.MAX_VALUE else dirpadMoves(ymoves + xmoves + ACTIVATE, depth - 1),
                )

                fromPos = toPos
            }

            size
        }
    }

    fun part1(input: List<String>): Long {
        return input.sumOf { line ->
            var grid = ("A" + line).zipWithNext { a, b -> numpadMoves(a, b) }.flatten()
            dirpadMoves(grid, 2) * line.dropLast(1).dropWhile { it == '0' }.toLong()
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { line ->
            var code = ("A" + line).zipWithNext { a, b -> numpadMoves(a, b) }.flatten()
            dirpadMoves(code, 25) * line.dropLast(1).dropWhile { it == '0' }.toLong()
        }
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("Day01_test_desc")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("day21/Day21_test")
    check(part1(testInput).println() == 126384L)
//    check(part2(testInput).println() == 1206)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("day21/Day21")
    check(part1(input).println() == 125742L)
    part2(input).println()
}
