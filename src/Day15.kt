enum class BLOCK(val char: Char) {
    WALL('#'),
    EMPTY('.'),
    GOOD('O'),
    ROBOT('@'),
    GOODL('['),
    GOODR(']');

    companion object {
        fun fromChar(char: Char) = entries.first { it.char == char }
    }
}

enum class DIR(val char: Char, val dx: Int, val dy: Int) {
    UP('^', 0, -1),
    DOWN('v', 0, 1),
    LEFT('<', -1, 0),
    RIGHT('>', 1, 0);

    companion object {
        fun fromChar(char: Char) = entries.first { it.char == char }
    }
}


fun main() {

    fun printGrid(gridSize: Point, grid: Map<Point, BLOCK>) {
        for (y in 0..<gridSize.y) {
            for (x in 0..<gridSize.x) {
                print(grid[Point(x, y)]?.char ?: ' ')
            }
            println()
        }
    }

    fun push(posList: List<Point>, dir: DIR, grid: Map<Point, BLOCK>): List<Point>? {
        val nextBlockLayer = mutableListOf<Point>()
        for (pos in posList) {
            val nextPos = Point(pos.x + dir.dx, pos.y + dir.dy)
            when (grid[nextPos]!!) {
                BLOCK.WALL -> return null
                BLOCK.GOOD -> nextBlockLayer += nextPos
                BLOCK.GOODL -> {
                    nextBlockLayer += nextPos
                    if (dir == DIR.UP || dir == DIR.DOWN) nextBlockLayer += Point(nextPos.x + 1, nextPos.y)
                }

                BLOCK.GOODR -> {
                    nextBlockLayer += nextPos
                    if (dir == DIR.UP || dir == DIR.DOWN) nextBlockLayer += Point(nextPos.x - 1, nextPos.y)
                }

                BLOCK.ROBOT -> error("Can't be more than 2 Robots")
                BLOCK.EMPTY -> {}
            }
        }

        if (nextBlockLayer.isNotEmpty()) {
            val push = push(nextBlockLayer, dir, grid) ?: return null
            return nextBlockLayer + push
        } else {
            return posList
        }
    }

    fun solve(grid: MutableMap<Point, BLOCK>, moves: List<DIR>, scoreBlock: BLOCK): Int {
        var robotPos = grid.filterValues { it == BLOCK.ROBOT }.keys.first()

        val blocksToPush = mutableListOf<Point>()

        for (move in moves) {
            val push2 = push(listOf(robotPos), move, grid)?.plus(robotPos)?.distinct()

            push2?.sortedBy {
                when (move) {
                    DIR.UP -> it.y
                    DIR.DOWN -> 0 - it.y
                    DIR.LEFT -> it.x
                    DIR.RIGHT -> 0 - it.x
                }
            }?.forEachIndexed { index, point ->
                grid[Point(point.x + move.dx, point.y + move.dy)] = grid[point]!!
                if (index == push2.size - 1) robotPos = Point(point.x + move.dx, point.y + move.dy)
                grid[point] = BLOCK.EMPTY
            }

            blocksToPush.clear()
        }

        return grid.filterValues { it == scoreBlock }.keys.sumOf { it.y * 100 + it.x }
    }

    fun part1(input: List<String>): Int {
        val (gridInput, movesInput) = input.filter { it.isNotEmpty() }.partition { it.startsWith(BLOCK.WALL.char) }
        val grid =
            gridInput.flatMapIndexed { y, line -> line.mapIndexed { x, char -> Point(x, y) to BLOCK.fromChar(char) } }
                .toMap().toMutableMap()
        val moves = movesInput.flatMap { it.map { c -> DIR.fromChar(c) } }
        return solve(grid, moves, BLOCK.GOOD)
    }

    fun part2(input: List<String>): Int {
        val (gridInput, movesInput) = input.filter { it.isNotEmpty() }.partition { it.startsWith(BLOCK.WALL.char) }
        val wideGridInput = gridInput.map { line ->
            line.flatMap {
                when (it) {
                    BLOCK.GOOD.char -> listOf(BLOCK.GOODL.char, BLOCK.GOODR.char)
                    BLOCK.ROBOT.char -> listOf(BLOCK.ROBOT.char, BLOCK.EMPTY.char)
                    BLOCK.WALL.char -> listOf(BLOCK.WALL.char, BLOCK.WALL.char)
                    BLOCK.EMPTY.char -> listOf(BLOCK.EMPTY.char, BLOCK.EMPTY.char)
                    else -> error("Invalid char")
                }
            }
        }

        val grid = wideGridInput.flatMapIndexed { y, line -> line.mapIndexed { x, char -> Point(x, y) to BLOCK.fromChar(char) } }.toMap().toMutableMap()
        val moves = movesInput.flatMap { it.map { c -> DIR.fromChar(c) } }
        return solve(grid, moves, BLOCK.GOODL)
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("Day01_test_desc")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day15_test")
    check(part1(testInput).println() == 10092)
    check(part2(testInput).println() == 9021)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}
