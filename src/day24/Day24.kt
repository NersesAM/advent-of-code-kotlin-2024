package day24

import println
import readInput

typealias Wire = String

enum class Op {
    AND, OR, XOR;

    companion object {
        fun fromString(str: String) = entries.first { it.name == str.uppercase() }
    }
}

data class Gate(val a: Wire, val op: Op, val b: Wire)

fun main() {
    fun part1(input: List<String>): Long {
        val index = input.indexOf("")
        val wires = input.slice(0..<index)
            .associateTo(mutableMapOf<Wire, Long>()) { val (a, b) = it.split(": "); a to b.toLong() }
        val gates = input.slice(index + 1..<input.size).associate {
            val (a, op, b, r) = """(\w+) (\w+) (\w+) -> (\w+)""".toRegex().matchEntire(it)!!.destructured
            r as Wire to Gate(a, Op.fromString(op), b)
        }

        fun find(wire: Wire): Long = wires.getOrPut(wire) {
            val gate = gates.getValue(wire)
            when (gate.op) {
                Op.AND -> find(gate.a) and find(gate.b)
                Op.OR -> find(gate.a) or find(gate.b)
                Op.XOR -> find(gate.a) xor find(gate.b)
            }
        }

        return gates.keys.filter { it.startsWith("z") }
            .sumOf { find(it) shl it.drop(1).toInt() }
    }

    fun part2(input: List<String>): String {
        val index = input.indexOf("")
        val gates = input.slice(index + 1..<input.size).associate {
            val (a, op, b, r) = """(\w+) (\w+) (\w+) -> (\w+)""".toRegex().matchEntire(it)!!.destructured
            // sorting the gates to make it easier to search in them
            r as Wire to if (a < b) Gate(a, Op.fromString(op), b) else Gate(b, Op.fromString(op), a)
        }.toMutableMap()

        // Full adder logic gate
        //
        //    firstResult = x xor y
        //    firstCarry = x and y
        //    z == firstResult xor carry
        //    secondCarry = firstResult and carry
        //    carry = firstCarry or secondCarry

        var carry = gates.entries.first { (_, v) -> v.a == "x00" && v.b == "y00" && v.op == Op.AND }.key

        val badWires = mutableListOf<Wire>()

        // too lazy to implement x00 and y00 half adder logic, checked it manually
        for (i in 1..44) {
            val paddedIndex = i.toString().padStart(2, '0')
            val x = "x$paddedIndex"
            val y = "y$paddedIndex"
            val z = "z$paddedIndex"

            val zGate = gates.entries.first { (k, _) -> k == z }
            var firstResult = gates.entries.first { (_, v) -> v.a == x && v.b == y && v.op == Op.XOR }
            var firstCarry = gates.entries.first { (_, v) -> v.a == x && v.b == y && v.op == Op.AND }

            fun swapBadGates(bad: Map.Entry<Wire, Gate>, swap: Map.Entry<Wire, Gate>) {
                println("Bad Gate $bad")
                println("Swap Gate $swap")
                badWires.add(bad.key)
                badWires.add(swap.key)
                val temp = swap.value
                gates.put(swap.key, bad.value)
                gates.put(bad.key, temp)
                firstResult = gates.entries.first { (_, v) -> v.a == x && v.b == y && v.op == Op.XOR }
                firstCarry = gates.entries.first { (_, v) -> v.a == x && v.b == y && v.op == Op.AND }
            }

            if (zGate.value.op != Op.XOR) {
                println("Bad wires at $z wire is not connected to an XOR gate")
                val swap = gates.entries.first { (_, v) -> v.a == minOf(firstResult.key, carry) && v.b == maxOf(firstResult.key, carry) && v.op == Op.XOR }
                swapBadGates(zGate, swap)
            } else if (zGate.value.a != firstResult.key && zGate.value.b != firstResult.key) {
                println("Bad wires at $z where one of the XOR gates is not connected to the output of ($x XOR $y) firstResult")
                val swapFirstResultWire = if (zGate.value.a == carry) zGate.value.b else zGate.value.a
                val swap = gates.entries.first { (k, _) -> k == swapFirstResultWire }
                swapBadGates(firstResult, swap)
            }

            val secondCarry = gates.entries.first { (_, v) -> v.a == minOf(firstResult.key, carry) && v.b == maxOf(firstResult.key, carry) && v.op == Op.AND }.key
            carry = gates.entries.first { (_, v) -> v.a == minOf(firstCarry.key, secondCarry) && v.b == maxOf(firstCarry.key, secondCarry) && v.op == Op.OR }.key
        }

        return badWires.sorted().joinToString(",")
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("Day01_test_desc")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("day24/Day24_test")
    check(part1(testInput).println() == 2024L)
//    check(part2(testInput).println() == 1206)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("day24/Day24")
    part1(input).println()
    println()
    part2(input).println()
}
