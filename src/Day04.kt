fun main() {
    val xmas = "XMAS".toRegex()
    val samx = "SAMX".toRegex()
    fun xmasCount(string: String): Int = xmas.findAll(string).count() + samx.findAll(string).count()

    fun part1(input: List<String>): Int {
        var cnt = 0
        val n = input.size - 1
        val range = 0..n
        cnt += input.sumOf { xmasCount(it) }
        for (i in range) {
            var column = ""
            var diagonal1 = ""
            var diagonal2 = ""
            var crossdiagonal1 = ""
            var crossdiagonal2 = ""
            for (j in range) {
                column += input[j][i]
                if (i + j in range) {
                    diagonal1 += input[i + j][j]
                    crossdiagonal1 += input[i + j][n - j]
                    if (i != 0) {
                        diagonal2 += input[j][i + j]
                        crossdiagonal2 += input[j][n - i - j]
                    }
                }
            }
            cnt += xmasCount(column) + xmasCount(diagonal1) + xmasCount(diagonal2) + xmasCount(crossdiagonal1) + xmasCount(
                crossdiagonal2
            )
        }
        return cnt
    }

    fun part2(input: List<String>): Int {
        val crossMAS = "M.M.A.S.S|M.S.A.M.S|S.M.A.S.M|S.S.A.M.M".toRegex()
        var cnt = 0
        for (i in 0..<input.size - 2) {
            for (j in 0..<input[i].length - 2) {
                var square = ""
                for (k in 0..2) {
                    square += input[i + k].slice(j..j + 2)
                }
                square.println()
                if (square.matches(crossMAS)) cnt++
            }
        }
        return cnt
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("Day01_test_desc")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day04_test")
    check(part1(testInput).also { it.println() } == 18)
    check(part2(testInput).also { it.println() } == 9)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
