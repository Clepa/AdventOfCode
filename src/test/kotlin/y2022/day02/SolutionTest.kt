package y2022.day02

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SolutionTest {
  private val frequencyRounds = hashMapOf("A Y" to 1, "B X" to 1, "C Z" to 1)

  @Nested
  inner class Part1 {
    @Test
    fun `should return 15 points given match 'A Y', 'B X' and 'C Z'`() = assertEquals(15, part1(frequencyRounds))
  }

  @Nested
  inner class Part2 {
    @Test
    fun `should return 12 points given match 'A Y', 'B X' and 'C Z'`() = assertEquals(12, part2(frequencyRounds))
  }
}
