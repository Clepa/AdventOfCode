package y2022.day04

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SolutionTest {
  private val input = listOf("2-4,6-8", "2-3,4-5", "5-7,7-9", "2-8,3-7", "6-6,4-6", "2-6,4-8")

  @Nested
  inner class Part1 {
    @Test
    fun `should have 2 fully contained range pairs`() = assertEquals(2, part1(input))
  }

  @Nested
  inner class Part2 {
    @Test
    fun `should have 4 overlapping range pairs`() = assertEquals(4, part2(input))
  }
}
