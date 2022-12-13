package y2022.day13

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day13Test {
  private val input = listOf(
    "[1,1,3,1,1]", "[1,1,5,1,1]", "", "[[1],[2,3,4]]", "[[1],4]", "", "[9]", "[[8,7,6]]", "", //
    "[[4,4],4,4]", "[[4,4],4,4,4]", "", "[7,7,7,7]", "[7,7,7]", "", "[]", "[3]", "", "[[[]]]", "[[]]", "", //
    "[1,[2,[3,[4,[5,6,7]]]],8,9]", "[1,[2,[3,[4,[5,6,0]]]],8,9]",
  )

  @Nested
  inner class Part1 {
    @Test
    fun `should the sum of indices of pairs in correct order be 13`() = assertEquals(13, part1(input))
  }

  @Nested
  inner class Part2 {
    @Test
    fun `should the decoder key be 140 (10th and 14th)`() = assertEquals(140, part2(input))
  }
}
