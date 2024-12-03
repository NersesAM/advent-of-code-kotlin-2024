fun main() {
    val mulRegex = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()
    val doRegex = """do\(\)""".toRegex()
    val dontRegex = """don't\(\)""".toRegex()
    fun part1(input: List<String>): Int {
        return input.flatMap { mulRegex.findAll(it) }.sumOf { it.groupValues[1].toInt() * it.groupValues[2].toInt() }
    }

    fun part2(input: List<String>): Int {
        val oneline = input.joinToString(" ")
        val dos = doRegex.findAll(oneline).map { it.range.first to true }
        val donts = dontRegex.findAll(oneline).map { it.range.first to false }

        val flags = listOf((0 to true), (oneline.length to true)).plus(dos).plus(donts).sortedBy { it.first }

        val zones = flags.zipWithNext().map { (a, b) -> (a.first..b.first) to a.second }

        println(dos)
        println(donts)
        println(flags)

        return mulRegex.findAll(oneline).filter { match -> zones.any { (range, bool) -> range.first() <= match.range.first() && range.last >= match.range.last() && bool } }
            .sumOf { it.groupValues[1].toInt() * it.groupValues[2].toInt() }
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("Day01_test_desc")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day03_test")
    check(part2(testInput) == 48)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
