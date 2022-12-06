package y2022.day06

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

class SolutionTest {
  @Nested
  inner class Part1 {
    private val inputs = listOf(
      Pair("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 7),
      Pair("bvwbjplbgvbhsrlpgdmjqwftvncz", 5),
      Pair("nppdvjthqldpwncqszvftbrmjlhg", 6),
      Pair("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 10),
      Pair("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 11),
    )

    @TestFactory
    fun `should return correct first marker position`() = inputs.map {
      DynamicTest.dynamicTest("""when "${it.first}" should return ${it.second}""") {
        assertEquals(it.second, part1(it.first))
      }
    }
  }

  @ParameterizedTest(name = "when {0} should return {1}")
  @MethodSource("part2Arguments")
  fun `Part2 - should return correct first marker position`(input: String, expected: Int) =
    assertEquals(expected, part2(input))

  companion object {
    // With @TestInstance(PER_CLASS) annotation, this can be changed to a non-static method.
    @JvmStatic
    fun part2Arguments(): List<Arguments> = listOf(
      Arguments.of("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 19),
      Arguments.of("bvwbjplbgvbhsrlpgdmjqwftvncz", 23),
      Arguments.of("nppdvjthqldpwncqszvftbrmjlhg", 23),
      Arguments.of("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 29),
      Arguments.of("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 26)
    )
  }
}
