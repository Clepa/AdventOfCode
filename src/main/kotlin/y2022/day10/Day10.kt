package y2022.day10

fun main() {
  val input = java.io.File("src/main/kotlin/y2022/day10/input.txt").readLines()

  // Part 1.
  println(part1(input)) // 15120.

  // Part 2.
  println(part2(input)) // RKPJBPLA.
  /* ###..#..#.###....##.###..###..#.....##..
   * #..#.#.#..#..#....#.#..#.#..#.#....#..#.
   * #..#.##...#..#....#.###..#..#.#....#..#.
   * ###..#.#..###.....#.#..#.###..#....####.
   * #.#..#.#..#....#..#.#..#.#....#....#..#.
   * #..#.#..#.#.....##..###..#....####.#..#. */
}

fun part1(input: List<String>): Int {
  val instructions = input.map(Instruction::create)
  val cpu = CPU(program = instructions)

  repeat(220) { cpu.nextCycle() }

  return (20..220 step 40).toList().sumOf { it * cpu.xHistoric[it - 1] }
}

fun part2(input: List<String>): String {
  val instructions = input.map(Instruction::create)
  val cpu = CPU(program = instructions)
  val crt = CRT(List(6) { MutableList(40) { "" } })

  repeat(240) {
    cpu.nextCycle()
    crt.nextCycle(cpu.x - 1..cpu.x + 1) // `x` is sprite middle position.
  }

  return crt.monitor.joinToString("\n") { it.joinToString("") }
}

private data class CPU(var x: Int = 1, private val program: List<Instruction>) {
  val xHistoric = mutableListOf<Int>()

  private var pc = 0
  private var instructionCyclesLeft = 0

  fun nextCycle() {
    if (instructionCyclesLeft == 0) { //
      /* Although it is wrong to update `x` in the next cycle and not at the end when `instructionCyclesLeft == 0`,
       * it simplifies the implementation and is enough for this problem. */
      if (pc > 0 && program[pc - 1].type == InstructionType.ADDX) {
        x += program[pc - 1].value!!
      }
      pc++
      instructionCyclesLeft = program[pc - 1].nCycles
    }

    instructionCyclesLeft--
    xHistoric.add(x)
  }
}

private data class CRT(val monitor: List<MutableList<String>>) {
  private var cycle = 0
  private val wide: Int = monitor[0].size

  fun nextCycle(sprite: IntRange) {
    val i = cycle / wide
    val j = cycle % wide
    monitor[i][j] = if (j in sprite) LIT_PIXEL else DARK_PIXEL
    cycle++
  }

  companion object {
    private const val LIT_PIXEL = "#"
    private const val DARK_PIXEL = "."
  }
}

private data class Instruction(val type: InstructionType, val nCycles: Int, val value: Int? = null) {
  companion object {
    @JvmStatic
    fun create(line: String): Instruction {
      val elements = line.split(" ")
      return when (val type = InstructionType.fromString(elements[0])) {
        InstructionType.ADDX -> Instruction(type, 2, elements[1].toInt())
        InstructionType.NOOP -> Instruction(type, 1)
      }
    }
  }
}

private enum class InstructionType(val str: String) {
  ADDX("addx"), NOOP("noop");

  companion object {
    private val lookup = InstructionType.values().associateBy(InstructionType::str)
    fun fromString(str: String): InstructionType =
      requireNotNull(lookup[str]) { """No InstructionType with string "$str". Valid values: ${InstructionType.lookup.keys}""" }
  }
}
