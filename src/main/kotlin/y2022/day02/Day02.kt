package y2022.day02

import java.io.File

// The mappings can be generalized and automatically created.
// A > C > B > A: X > C, Y > A, Z > B.
// A - rock, B - paper, C - scissors (ad).
// X - rock, Y - paper, Z - scissors (me).
// X - lose, Y - draw,  Z - win      (p2).

private const val WIN = 6
private const val DRAW = 3
private const val LOSE = 0

val handShapePoints = hashMapOf("X" to 1, "Y" to 2, "Z" to 3)
val scoreMatchTable = hashMapOf(
  "A X" to DRAW, "B X" to LOSE, "C X" to WIN, //
  "A Y" to WIN, "B Y" to DRAW, "C Y" to LOSE, //
  "A Z" to LOSE, "B Z" to WIN, "C Z" to DRAW
)

fun main() {
  val input = File("src/main/kotlin/y2022/day02/input.txt").readLines()
  val frequencyMatches = input.groupingBy { it }.eachCount()

  // Part 1.
  println(part1(frequencyMatches)) // 13005.

  // Part 2.
  println(part2(frequencyMatches)) // 11373.
}

fun part1(frequencyMatches: Map<String, Int>): Int = calculateScore(frequencyMatches)

fun part2(frequencyMatches: Map<String, Int>): Int {
  val newMappings = hashMapOf(
    "A X" to "A Z", "B X" to "B X", "C X" to "C Y", //
    "A Y" to "A X", "B Y" to "B Y", "C Y" to "C Z", //
    "A Z" to "A Y", "B Z" to "B Z", "C Z" to "C X"
  )

  val newFrequencies = frequencyMatches.mapKeys { newMappings.getOrElse(it.key) { throw Error("Invalid match: $it") } }
  return calculateScore(newFrequencies)
}

private fun calculateScore(frequencyMatches: Map<String, Int>) = frequencyMatches.entries.sumOf {
  val selection = handShapePoints.getOrElse(it.key.last().toString()) { throw Error("Invalid hand shape: $it") }
  val match = scoreMatchTable.getOrElse(it.key) { throw Error("Invalid match: $it") }
  (selection + match) * it.value
}
