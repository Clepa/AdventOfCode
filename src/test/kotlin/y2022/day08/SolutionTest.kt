package y2022.day08

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SolutionTest {
  private val input = listOf("30373", "25512", "65332", "33549", "35390")

  @Nested
  inner class Part1 {
    @Test
    fun `should have 21 visible trees from outside the grid `() = assertEquals(21, part1(input))
  }

  @Nested
  inner class Part2 {
    @Test
    fun `should scenic score be 8`() = assertEquals(8, part2(input))
  }
}
