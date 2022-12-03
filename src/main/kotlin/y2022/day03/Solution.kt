package y2022.day03

import java.io.File

fun main() {
  val input = File("src/main/kotlin/y2022/day03/input.txt").readLines()

  // Part 1.
  println(part1(input)) // 7746.

  // Part 2.
  println(part2(input)) // 2604.
}

fun part1(input: List<String>): Int =
  input.map { it.chunked(it.length / 2) }.map { it.map(String::toSet) }.flatMap { it[0].intersect(it[1]) }
    .sumOf(::normalizeCharCode)

fun part2(input: List<String>): Int =
  input.map(String::toSet).chunked(3).flatMap { it.reduce { acc, chars -> acc.intersect(chars) } }
    .sumOf(::normalizeCharCode)

// a - z: 1..26, A-Z: 27..52.
private fun normalizeCharCode(char: Char): Int = when (val code = char.code) {
  in 65..90 -> code - 65 + 27
  in 97..122 -> code - 97 + 1
  else -> throw Error("Invalid char code: $char with $code")
}
