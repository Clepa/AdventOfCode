package y2022.day11

fun main() {
  val input = java.io.File("src/main/kotlin/y2022/day11/input.txt").readLines()

  // Part 1.
  println(part1(input)) // 117640.

  // Part 2.
  println(part2(input)) // 30616425600.
}

fun part1(input: List<String>): Long {
  val monkeys = createMonkeys(input)

  repeat(20) {
    monkeys.forEach { monkey -> monkey.runTurn().forEach { monkeys[it.first].items.addLast(it.second) } }
  }

  return monkeys.map(Monkey::nInspectedItems).sortedDescending().take(2).reduce { acc, value -> acc * value }
}

fun part2(input: List<String>): Long {
  val monkeys = createMonkeys(input)

  repeat(10000) {
    monkeys.forEach { monkey -> monkey.runTurn(false).forEach { monkeys[it.first].items.addLast(it.second) } }
  }

  return monkeys.map(Monkey::nInspectedItems).sortedDescending().take(2).reduce { acc, value -> acc * value }
}

private fun createMonkeys(input: List<String>): List<Monkey> {
  val filterDigit = { str: String -> str.split(":")[1].filter(Char::isDigit).toInt() }

  val divisibles = input.slice(3..input.lastIndex step 7).map { filterDigit(it) }
  val lcm = lcm(divisibles)

  val monkeys = input.chunked(7).mapIndexed { index, chunk ->
    val items = ArrayDeque(chunk[1].split(":")[1].split(",").map { Item(it.trim().toLong()) })
    val operation = chunk[2].split("=")[1].trim().split(" ")
    val throwToMonkey = Pair(filterDigit(chunk[4]), filterDigit(chunk[5]))

    Monkey(items, divisibles[index], throwToMonkey) { worry ->
      val operands = listOf(operation[0], operation[2]).map { if (it == "old") worry else it.toLong() }
      Operation(operation[1], operands[0], operands[1]).calculate() % lcm
    }
  }

  return monkeys
}

private data class Monkey(
  val items: ArrayDeque<Item>,
  private val divisible: Int,
  private val throwToMonkey: Pair<Int, Int>, // First: if divisible, second: if not divisible.
  private val updateWorryAction: (oldWorry: Long) -> Long
) {
  var nInspectedItems = 0L

  fun runTurn(shouldDecrease: Boolean = true): List<Pair<Int, Item>> = items.indices.map {
    updateWorryLevel()
    if (shouldDecrease) {
      decreaseWorryLevel()
    }
    throwItem()
  }

  private fun updateWorryLevel() {
    nInspectedItems++
    val item = items.first()
    item.worry = updateWorryAction(item.worry)
  }

  private fun decreaseWorryLevel() {
    items.first().worry /= 3
  }

  private fun throwItem(): Pair<Int, Item> {
    val throwWhom = if (isDivisible()) throwToMonkey.first else throwToMonkey.second
    return Pair(throwWhom, items.removeFirst())
  }

  private fun isDivisible() = items.first().worry % divisible == 0L
}

private data class Item(var worry: Long)

private data class Operation(val operator: String, val leftOperand: Long, val rightOperand: Long) {
  fun calculate(): Long = when (operator) {
    "+" -> Math.addExact(leftOperand, rightOperand)
    "*" -> Math.multiplyExact(leftOperand, rightOperand)
    else -> throw Error("""Unexpected operand "$operator[1]"""")
  }
}

private fun lcm(numbers: List<Int>) = numbers.reduce { acc, value -> lcm(acc, value) }
private fun lcm(a: Int, b: Int): Int = a / gcd(a, b) * b
private fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)
