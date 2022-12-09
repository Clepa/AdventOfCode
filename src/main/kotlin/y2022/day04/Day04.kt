package y2022.day04

import java.io.File

// We can assign input.map(::mapLineToPairRanges) to a variable to avoid repeating the mapping.
fun main() {
  val input = File("src/main/kotlin/y2022/day04/input.txt").readLines()

  // Part 1.
  println(part1(input)) // 485.

  // Part 2.
  println(part2(input)) // 857.
}

fun part1(input: List<String>): Int = input.map(::mapLineToPairRanges).filter(::oneRangeFullyContainsOther).size

fun part2(input: List<String>): Int = input.map(::mapLineToPairRanges).filter(::oneRangeOverlapsOther).size

private fun mapLineToPairRanges(line: String): Pair<IntRange, IntRange> {
  val ranges = line.split(",").map { it.split("-").map(String::toInt) }.map { IntRange(it[0], it[1]) }
  return Pair(ranges[0], ranges[1])
}

private fun oneRangeFullyContainsOther(ranges: Pair<IntRange, IntRange>): Boolean {
  val contains = { r1: IntRange, r2: IntRange -> r1.contains(r2.first) && r1.contains(r2.last) }
  return contains(ranges.first, ranges.second) || contains(ranges.second, ranges.first)
}

private fun oneRangeOverlapsOther(ranges: Pair<IntRange, IntRange>): Boolean {
  val contains = { r1: IntRange, r2: IntRange -> r1.contains(r2.first) || r1.contains(r2.last) }
  return contains(ranges.first, ranges.second) || contains(ranges.second, ranges.first)
}
