package y2022.day01

import java.io.File

fun main() {
  val input = File("src/main/kotlin/y2022/day01/input.txt").readText()

  val sums = input.trim().split("(\r?\n){2}".toRegex()).map {
    it.split("\r?\n".toRegex()).map(String::toInt).sum()
  }.sortedDescending()

  // Part 1.
  println(sums[0]) // 66186.

  // Part 2.
  println(sums.slice(0..2).sum()) // 196804.
}
