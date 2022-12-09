package y2022.day08

import kotlin.math.abs

fun main() {
  val input = java.io.File("src/main/kotlin/y2022/day08/input.txt").readLines()

  // Part 1.
  println(part1(input)) // 1787.

  // Part 2.
  println(part2(input)) // 440640.
}

fun part1(input: List<String>): Int {
  val grid = input.map { it.toCharArray().map(Char::digitToInt) }
  return calculateGridVisibleTrees(grid).flatten().groupingBy { it }.eachCount().getValue(true)
}

fun part2(input: List<String>): Int {
  val grid = input.map { it.toCharArray().map(Char::digitToInt) }
  return calculateTreesScenicScore(grid).flatten().max()
}

private fun calculateGridVisibleTrees(grid: List<List<Int>>): Array<Array<Boolean>> {
  val gridVisibleTrees = Array(grid.size) { Array(grid[0].size) { false } }

  updateGridVisibleTrees(gridVisibleTrees, grid, false)
  updateGridVisibleTrees(gridVisibleTrees, grid, true)

  return gridVisibleTrees
}

private fun updateGridVisibleTrees(gridVisibleTrees: Array<Array<Boolean>>, grid: List<List<Int>>, reversed: Boolean) {
  val indices = { list: List<*> -> if (!reversed) list.indices else list.indices.reversed() }
  val updateGridVisibleTrees = { i: Int, j: Int, currentMax: Int ->
    if (grid[i][j] > currentMax) {
      if (!gridVisibleTrees[i][j]) { // If we do not care about assigning when not needed, the `if` can be deleted.
        gridVisibleTrees[i][j] = true
      }
      grid[i][j]
    } else {
      currentMax
    }
  }

  var currentMaxRowHeight = -1
  var currentMaxColumnHeight = -1
  for (i in indices(grid)) {
    for (j in indices(grid[i])) {
      currentMaxRowHeight = updateGridVisibleTrees(i, j, currentMaxRowHeight)
      currentMaxColumnHeight = updateGridVisibleTrees(j, i, currentMaxColumnHeight)
    }
    currentMaxRowHeight = -1
    currentMaxColumnHeight = -1
  }
}

private fun calculateTreesScenicScore(grid: List<List<Int>>): Array<Array<Int>> {
  val scenicScores = Array(grid.size) { Array(grid[0].size) { 0 } }

  for (i in 1 until grid.lastIndex) {
    for (j in 1 until grid[i].lastIndex) {
      scenicScores[i][j] = calculateTreeScenicScore(grid, i, j)
    }
  }

  return scenicScores
}

@Suppress("ControlFlowWithEmptyBody")
private fun calculateTreeScenicScore(grid: List<List<Int>>, i: Int, j: Int): Int {
  if (i == 0 || j == 0) {
    return 0
  }

  val tree = grid[i][j]

  var up = i
  while (up > 0 && tree > grid[--up][j]);

  var down = i
  while (down < grid.size - 1 && tree > grid[++down][j]);

  var left = j
  while (left > 0 && tree > grid[i][--left]);

  var right = j
  while (right < grid[i].size - 1 && tree > grid[i][++right]);

  return abs((up - i) * (down - i) * (right - j) * (left - j))
}
