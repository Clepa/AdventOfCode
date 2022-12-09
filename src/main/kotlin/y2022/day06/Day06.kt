package y2022.day06

import java.io.File

fun main() {
  val input = File("src/main/kotlin/y2022/day06/input.txt").readLines()[0]

  // Part 1.
  println(part1(input)) // 1876.

  // Part 2.
  println(part2(input)) // 2202.
}

fun part1(input: String): Int = input.windowed(4).indexOfFirst { it.toSet().size == 4 } + 4

// Optimal solution.
fun part2(input: String): Int {
  var windowIdx = -1
  for (i in 0 until input.length - 13) {
    if (input.substring(i..i + 13).toSet().size == 14) {
      windowIdx = i
      break
    }
  }
  return if (windowIdx != -1) windowIdx + 14 else -1
}
