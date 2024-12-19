package day17

import println
import readInput

enum class Opcode(val value: Int) {
    ADV(0),
    BXL(1),
    BST(2),
    JNZ(3),
    BXC(4),
    OUT(5),
    BDV(6),
    CDV(7);

    companion object {
        private val entryMap = entries.associateBy { it.value }
        fun fromInt(value: Int): Opcode = entryMap[value] ?: error("Invalid value: $value")
    }
}

data object REGISTER {
    var A = 0L
    var B = 0L
    var C = 0L
}

typealias Operand = Long

data class Instruction(val opcode: Opcode, val operand: Operand)

typealias Program = List<Instruction>

fun main() {
    fun comboOperand(operand: Operand): Operand = when (operand) {
        in 0..3 -> operand
        4L -> REGISTER.A
        5L -> REGISTER.B
        6L -> REGISTER.C
        else -> error("Invalid operand: $operand")
    }

    fun parseInputAndSetRegister(input: List<String>): Program {
        REGISTER.A = input[0].substringAfter(": ").toLong()
        REGISTER.B = input[1].substringAfter(": ").toLong()
        REGISTER.C = input[2].substringAfter(": ").toLong()
        return input[4].substringAfter(": ").split(',').windowed(2, 2)
            .map { Instruction(Opcode.fromInt(it[0].toInt()), it[1].toLong()) }
    }

    fun divby2pow(numerator: Long, exp: Long): Long = when {
        exp > 31 -> 0
        else -> numerator shr exp.toInt()
    }

    fun solve(program: Program): List<Long> {
        val output = mutableListOf<Long>()
        var instructionPointer = 0

        while (instructionPointer / 2 < program.size) {
            val instruction = program[instructionPointer / 2]
            val comboOperand = comboOperand(instruction.operand)
            when (instruction.opcode) {
                Opcode.ADV -> REGISTER.A = divby2pow(REGISTER.A, comboOperand)
                Opcode.BXL -> REGISTER.B = REGISTER.B xor instruction.operand
                Opcode.BST -> REGISTER.B = comboOperand and 7
                Opcode.JNZ -> if (REGISTER.A != 0L) {
                    instructionPointer = instruction.operand.toInt(); continue
                }

                Opcode.BXC -> REGISTER.B = REGISTER.B xor REGISTER.C
                Opcode.OUT -> output.add(comboOperand and 7)
                Opcode.BDV -> REGISTER.B = divby2pow(REGISTER.A, comboOperand)
                Opcode.CDV -> REGISTER.C = divby2pow(REGISTER.A, comboOperand)
            }
            instructionPointer += 2
        }

        return output
    }

    fun part1(input: List<String>): List<Long> {
        return solve(parseInputAndSetRegister(input))
    }

    /*
     * 2,4, 1,5, 7,5, 4,5, 0,3, 1,6, 5,5, 3,0
     *
     * 0:  rB = rA and 7
     * 2:  rB = rB xor 5
     * 4:  rC = divby2pow(rA, rB)
     * 6:  rB = rB xor rC
     * 8:  rA = divby2pow(rA, 3)
     * 10: rB = rB xor 6
     * 12: out rB and 7
     * 14: if rA != 0 goto 0
     *
     * This means that every iteration of the program calculates the output based on the last 3 bits of the register A.
     * So we can start with A = 0 then increment it by 1 until we get the last output value.
     * Then we shift it by 3 bits to the left and keep incrementing until we get last 2 output values.
     * Repeat until we get all the output values.
     *
     */

    fun part2(input: List<String>): Long {
        val program = parseInputAndSetRegister(input)
        val expectedOutput = program.flatMap { listOf(it.opcode.value.toLong(), it.operand) }
        REGISTER.A = 0

        var a = 0L
        for (i in 0..<expectedOutput.size) {
            while (true) {
                REGISTER.A = a
                REGISTER.B = 0
                REGISTER.C = 0
                val s = solve(program)
                ("$a -> " + s).println()
                if (s != expectedOutput.drop(expectedOutput.size - 1 - i)) {
                    a++
                } else {
                    break
                }
            }
            a = a shl 3
        }
        return a shr 3
    }

    // Test if implementation meets criteria from the description, like:
//    check(part1(listOf("Day01_test_desc")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("day17/Day17_test")
    check(part1(testInput).println() == listOf(4L, 6, 3, 5, 6, 3, 5, 2, 1, 0))
    check(part2(readInput("day17/Day17p2_test")).println() == 117440L)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("day17/Day17")
    part1(input).println()
    part2(input)//.println()
}
