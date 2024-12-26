package day23

import println
import readInput

typealias Computer = String

fun main() {
    fun findNetworks(networks: Set<Set<Computer>>, graph: Map<Computer, Set<Computer>>, size: Int, maxSize: Int? = null): Set<Set<Computer>> {
        val newNetworks = mutableSetOf<Set<Computer>>()
        for (network in networks) {
            for (computer in graph.keys) {
                if (network.contains(computer)) continue
                if (graph.getValue(computer).containsAll(network)) {
                    newNetworks.add(network + computer)
                }
            }
        }

        if (newNetworks.isEmpty()) return networks

        return if(size == maxSize) newNetworks else findNetworks(newNetworks, graph, size + 1, maxSize)
    }

    fun part1(input: List<String>): Int {
        val graph = buildMap {
            input.forEach {
                val (a, b) = it.split("-");
                getOrPut(a) { mutableSetOf() }.add(b)
                getOrPut(b) { mutableSetOf() }.add(a)
            }
        } as Map<Computer, Set<Computer>>

        return findNetworks(graph.keys.map { setOf(it) }.toSet(), graph, 2, 3).count { it.any { it.startsWith("t")} }
    }

    fun part2(input: List<String>): String {
        val graph = buildMap {
            input.forEach {
                val (a, b) = it.split("-");
                getOrPut(a) { mutableSetOf() }.add(b)
                getOrPut(b) { mutableSetOf() }.add(a)
            }
        } as Map<Computer, Set<Computer>>

        return findNetworks(graph.keys.map { setOf(it) }.toSet(), graph, 2).first().sorted().joinToString(",")
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("Day01_test_desc")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("day23/Day23_test")
    check(part1(testInput).println() == 7)
    check(part2(testInput).println() == "co,de,ka,ta")

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("day23/Day23")
    part1(input).println()
    part2(input).println()
}
