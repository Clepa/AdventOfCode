package y2022.day05

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SolutionTest {
  private val input = listOf(
    "    [D]    ",
    "[N] [C]    ",
    "[Z] [M] [P]",
    " 1   2   3 ",
    "",
    "move 1 from 2 to 1",
    "move 3 from 1 to 3",
    "move 2 from 2 to 1",
    "move 1 from 1 to 2"
  )

  @Nested
  inner class Part1 {
    @Test
    fun `should top crates be CMZ`() = kotlin.test.assertEquals("CMZ", part1(input))
  }

  @Nested
  inner class Part2 {
    @Test
    fun `should top crates be MCD`() = kotlin.test.assertEquals("MCD", part2(input))
  }
}
