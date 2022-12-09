package y2022.day09

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day09Test {
  private val input = listOf("R 4", "U 4", "L 3", "D 1", "R 4", "D 1", "L 5", "R 2")

  @Nested
  inner class Part1 {
    @Test
    fun `should tail visited positions be 13`() = assertEquals(13, part1(input))
  }

  @Nested
  inner class Part2 {
    private val largerInput = listOf("R 5", "U 8", "L 8", "D 3", "R 17", "D 10", "L 25", "U 20")

    @Test
    fun `should tail visited positions be 1 (no move)`() = assertEquals(1, part2(input))

    @Test
    fun `should tail visited positions be 36`() = assertEquals(36, part2(largerInput))
  }
}
