fun main() {
    fun intToQueue(i: Int, rep: Int, q: ArrayDeque<Int>) {
        repeat(rep) {
            q.add(i)
        }
    }

    fun part1(input: List<String>): Long {
        var counter = 0
        var checksum = 0L

        fun addChecksum(i: Int) {
            checksum += i * counter++
        }

        val disk = input[0]
        var i = 0
        val leadingFileDeque = ArrayDeque<Int>()
        var gapCapacity = disk[i + 1].digitToInt()
        var fileLocation = disk.length - 1
        val fileDeque = ArrayDeque<Int>()
        intToQueue(fileLocation / 2, disk[fileLocation].digitToInt(), fileDeque)
        while (i < fileLocation) {
            intToQueue(i / 2, disk[i].digitToInt(), leadingFileDeque)
            while (leadingFileDeque.isNotEmpty()) {
                addChecksum(leadingFileDeque.removeFirst())
            }
            do {
                if (fileDeque.isNotEmpty()) {
                    if (gapCapacity == 0) break
                    addChecksum(fileDeque.removeLast())
                    gapCapacity--
                } else {
                    fileLocation -= 2
                    if (fileLocation <= i) break

                    intToQueue(fileLocation / 2, disk[fileLocation].digitToInt(), fileDeque)
                }
            } while (true)

            i += 2
            gapCapacity = disk.getOrElse(i + 1, { '0' }).digitToInt()
        }
        while (fileDeque.isNotEmpty()) {
            addChecksum(fileDeque.removeFirst())
        }
        return checksum
    }

    fun part2(input: List<String>): Long {
        val disk = input[0].mapIndexed { i, c -> c.digitToInt() to if (i % 2 == 0) i / 2 else null }.toMutableList()
        for (f in disk.size - 1 downTo 2 step 2) {
            var i = 1
            while (i < f) {
                if (disk[i].first >= disk[f].first) {
                    disk[i] = (disk[i].first - disk[f].first) to null // reduce the free space size
                    val fileF = disk.removeAt(f) // remove the file
                    disk.add(i, fileF) // insert the file at the beginning of the free space
                    val freeBeforeF = disk.removeAt(f) // remove the free space before the file at old location
                    disk.add(i, 0 to null)  // insert the 0-length free space at new location to preserve structure
                    if (f + 1 < disk.size) {  // expand the free space from where the file was moved
                        disk[f + 1] = (disk[f + 1].first + freeBeforeF.first + fileF.first) to null
                    }
                    i = 1
                    continue
                }
                i += 2
            }
        }

        var i = 0
        var sum = 0L
        for (f in disk) {
            repeat(f.first) {
                sum += (i + it) * (f.second ?: 0)
            }
            i += f.first
        }
        return sum
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("Day01_test_desc")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day09_test")
    check(part1(testInput).println() == 1928L)
    check(part2(testInput).println() == 2858L)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day09")
    part1(input).println() //6395800119709
    part2(input).println() //6418529470362
}
