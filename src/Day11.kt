fun main() {

    data class Value(val v: Long, val count: Long)

    fun nextLayer(number: Long): List<Long> = buildList {
        val l = number.toString().length
        if (number == 0L) {
            add(1)
        } else if (l % 2 == 0) {
            var divider = 1L
            repeat(l / 2) {
                divider *= 10
            }
            add(number / divider)
            add(number % divider)
        } else {
            add(number * 2024)
        }
    }

    fun solve(input: List<String>, depth: Int): Long {
        val numbers = input[0].split(' ').map { it.toLong() }

        val layers = mutableListOf(numbers.mapTo(mutableListOf()) { Value(it, 1L) })
        for (i in 1..depth) {
            val l = layers[i - 1]
                .flatMap { (v, c) -> nextLayer(v).map { Value(it, c) } }
                .groupBy { it.v }
                .mapTo(mutableListOf()){ Value(it.key, it.value.sumOf { it.count }) }
            layers.add(l)
        }
        return layers.last().sumOf { (_, count) -> count }.toLong()
    }

    fun part1(input: List<String>): Long {
        return solve(input, 25)
    }

    fun part2(input: List<String>): Long {
        return solve(input, 75)
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("Day01_test_desc")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day11_test")
    check(part1(testInput).println() == 55312L)
//    check(part2(testInput).println() == 1)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
