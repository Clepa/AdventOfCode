package y2022.day05

import java.io.File

/* We can assign stacks and actions to variables, and change the input of `part*` methods, to avoid processing input
 * lines multiple times. */
fun main() {
  val input = File("src/main/kotlin/y2022/day05/input.txt").readLines()

  // Part 1.
  println(part1(input)) // TGWSMRBPN.

  // Part 2.
  println(part2(input)) // TZLTLWRNF.
}

fun part1(input: List<String>): String {
  val stacks = obtainInputStacks(input)
  val actions = obtainInputActions(input)

  doActions(stacks, actions, ::doActionWith9000)

  return obtainFirstCrates(stacks)
}

fun part2(input: List<String>): String {
  val stacks = obtainInputStacks(input)
  val actions = obtainInputActions(input)

  doActions(stacks, actions, ::doActionWith9001)

  return obtainFirstCrates(stacks)
}

private fun obtainInputStacks(input: List<String>): List<ArrayDeque<String>> {
  val lines = input.subList(0, input.indexOfFirst { it.trim().startsWith('1') }).map { line ->
    line.chunked(4).map(String::trim).map { it.removeSurrounding("[", "]") }
  }

  val stacks = MutableList<ArrayDeque<String>>(lines[0].size) { ArrayDeque() }
  stacks.forEachIndexed { idx, el ->
    lines.forEach {
      val pile = it[idx]
      if (pile.isNotEmpty()) {
        el.addFirst(pile)
      }
    }
  }
  // Add an extra stack at the beginning to directly apply move actions.
  stacks.add(0, ArrayDeque())

  return stacks
}

private fun obtainInputActions(input: List<String>): List<Action> {
  val pattern = Regex("[0-9]+")
  return input.filter { it.startsWith("move") }
    .map { pattern.findAll(it).map(MatchResult::value).toList().map(String::toInt) }.map(::Action)
}

private fun doActions(
  stacks: List<ArrayDeque<String>>, actions: List<Action>, doAction: (List<ArrayDeque<String>>, actions: Action) -> Unit
) = actions.forEach { doAction(stacks, it) }

private fun doActionWith9000(stacks: List<ArrayDeque<String>>, action: Action) =
  repeat(action.numberOfCrates) { stacks[action.to].addLast(stacks[action.from].removeLast()) }

private fun doActionWith9001(stacks: List<ArrayDeque<String>>, action: Action) {
  val elements = mutableListOf<String>()
  repeat(action.numberOfCrates) { elements.add(stacks[action.from].removeLast()) }
  stacks[action.to].addAll(elements.reversed())
}

private fun obtainFirstCrates(stacks: List<ArrayDeque<String>>): String =
  stacks.filter(ArrayDeque<String>::isNotEmpty).joinToString("", transform = ArrayDeque<String>::last)

private data class Action(val numberOfCrates: Int, val from: Int, val to: Int) {
  constructor(elements: List<Int>) : this(elements[0], elements[1], elements[2])
}
