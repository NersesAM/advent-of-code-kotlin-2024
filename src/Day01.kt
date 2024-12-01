import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val firstList = ArrayList<Int>(input.size)
        val secondList = ArrayList<Int>(input.size)
        for (s in input) {
            s.split("   ").let {
                firstList.add(it[0].toInt())
                secondList.add(it[1].toInt())
            }
        }
        return secondList.sorted().zip(firstList.sorted()).sumOf { (a, b) -> abs(a - b) }
    }

    fun part2(input: List<String>): Int {
        val firstList = ArrayList<Int>(input.size)
        val secondList = ArrayList<Int>(input.size)
        for (s in input) {
            s.split("   ").let {
                firstList.add(it[0].toInt())
                secondList.add(it[1].toInt())
            }
        }
        val firstMap = firstList.groupingBy { it }.eachCount()
        val secondMap = secondList.groupingBy { it }.eachCount()
        return firstMap.map { (n, c) -> n * c * secondMap.getOrElse(n) { 0 } }.sum()
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
