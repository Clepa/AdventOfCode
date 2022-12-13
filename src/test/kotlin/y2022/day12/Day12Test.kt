package y2022.day12

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day12Test {
  private val input = listOf("Sabqponm", "abcryxxl", "accszExk", "acctuvwj", "abdefghi")

  @Nested
  inner class Part1 {
    @Test
    fun `should the shortest path starting at 'S' be of 31 steps`() = assertEquals(31, part1(input))
  }

  @Nested
  inner class Part2 {
    @Test
    fun `should the shortest path starting at any square be of 29 steps`() = assertEquals(29, part2(input))
  }
}
