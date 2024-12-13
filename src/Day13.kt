enum class Type(val cost: Int) {
    A(3),
    B(1)
}

fun main() {
    data class Button(val type: Type, val x: Int, val y: Int)
    data class Point(val x: Long, val y: Long)
    data class Puzzle(val buttonA: Button, val buttonB: Button, val prize: Point)

    // Very lazy and ugly parsing of the input
    fun parseInput(input: List<String>, prizeFix: Long = 0): List<Puzzle> {
        val digits = """\d+""".toRegex()
        return input.filter { it.isNotEmpty() }.windowed(3, 3).map {
            val buttonA = Button(
                Type.A,
                digits.findAll(it[0]).toList()[0].value.toInt(),
                digits.findAll(it[0]).toList()[1].value.toInt()
            )
            val buttonB = Button(
                Type.B,
                digits.findAll(it[1]).toList()[0].value.toInt(),
                digits.findAll(it[1]).toList()[1].value.toInt()
            )
            val prize = Point(
                digits.findAll(it[2]).toList()[0].value.toLong() + prizeFix,
                digits.findAll(it[2]).toList()[1].value.toLong() + prizeFix
            )
            Puzzle(buttonA, buttonB, prize)
        }
    }

    fun findTokens(puzzles: List<Puzzle>): Long {
        var tokens = 0L
        for (puzzle in puzzles) {
            val buttonA = puzzle.buttonA
            val buttonB = puzzle.buttonB
            val prize = puzzle.prize

            // prize.x = a * buttonA.x + b * buttonB.x
            // prize.y = a * buttonA.y + b * buttonB.y
            // a * buttonA.x = prize.x - b * buttonB.x
            // a * buttonA.y = prize.y - b * buttonB.y
            // a = (prize.x - b * buttonB.x) / buttonA.x
            // a = (prize.y - b * buttonB.y) / buttonA.y
            // (prize.x - b * buttonB.x) / buttonA.x = (prize.y - b * buttonB.y) / buttonA.y
            // (prize.x - b * buttonB.x) * buttonA.y = (prize.y - b * buttonB.y) * buttonA.x
            // prize.x * buttonA.y - b * buttonB.x * buttonA.y = prize.y * buttonA.x - b * buttonB.y * buttonA.x
            // b = (prize.x * buttonA.y - prize.y * buttonA.x) / (buttonB.x * buttonA.y - buttonB.y * buttonA.x)

            val b = (prize.x * buttonA.y - prize.y * buttonA.x) / (buttonB.x * buttonA.y - buttonB.y * buttonA.x)
            val a = (prize.x - b * buttonB.x) / buttonA.x

            if (prize.x == a * buttonA.x + b * buttonB.x && prize.y == a * buttonA.y + b * buttonB.y) {
                tokens += a * Type.A.cost + b * Type.B.cost
            }
        }


        return tokens
    }

    fun part1(input: List<String>): Long {
        return findTokens(parseInput(input))
    }

    fun part2(input: List<String>): Long {
        return findTokens(parseInput(input, 10000000000000L))
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("Day01_test_desc")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day13_test")
    check(part1(testInput).println() == 480L)
    check(part2(testInput).println() == 875318608908L)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}
