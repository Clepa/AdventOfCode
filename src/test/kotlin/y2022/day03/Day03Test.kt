package y2022.day03

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day03Test {
  private val input = listOf(
    "vJrwpWtwJgWrhcsFMMfFFhFp",
    "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL",
    "PmmdzqPrVvPwwTWBwg",
    "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn",
    "ttgJtRGJQctTZtZT",
    "CrZsJsPPZsGzwwsLwLmpwMDw"
  )

  @Nested
  inner class Part1 {
    @Test
    fun `should the sum of priorities be 157`() = assertEquals(157, part1(input))
  }

  @Nested
  inner class Part2 {
    @Test
    fun `should the sum of priorities be 70 grouping the rucksacks by 3`() = assertEquals(70, part2(input))
  }
}
