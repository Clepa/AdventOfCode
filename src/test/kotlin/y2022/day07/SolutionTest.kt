package y2022.day07

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SolutionTest {
  private val input = listOf(
    "$ cd /", "$ ls", "dir a", "14848514 b.txt", "8504156 c.dat", "dir d", "$ cd a", "$ ls", "dir e", "29116 f", //
    "2557 g", "62596 h.lst", "$ cd e", "$ ls", "584 i", "$ cd ..", "$ cd ..", "$ cd d", "$ ls", "4060174 j", //
    "8033020 d.log", "5626152 d.ext", "7214296 k"
  )

  @Nested
  inner class Part1 {
    @Test
    fun `should the sum of all directories of at most 100000 be 95437`() = kotlin.test.assertEquals(95437, part1(input))
  }

  @Nested
  inner class Part2 {
    @Test
    fun `should delete the directory with 24933642 size to free up enough space`() =
      kotlin.test.assertEquals(24933642, part2(input))
  }
}
