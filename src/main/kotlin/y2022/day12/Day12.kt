package y2022.day12

fun main() {
  val input = java.io.File("src/main/kotlin/y2022/day12/input.txt").readLines()

  // Part 1.
  println(part1(input)) // 490.

  // Part 2.
  println(part2(input)) // 488.
}

fun part1(input: List<String>): Int {
  val graph = Graph.createGraph(input) { vertex, neighbour -> vertex.elevation + 1 >= neighbour.elevation }
  val (startIdx, endIdx) = findStartEndIndices(input)

  return graph.breadthFirstSearchNSteps(
    graph.vertices[startIdx], graph.vertices[endIdx]
  ) { neighbour, end -> neighbour === end }
}

fun part2(input: List<String>): Int {
  val graph = Graph.createGraph(input) { vertex, neighbour -> vertex.elevation <= neighbour.elevation + 1 }
  val (_, end) = findStartEndIndices(input)

  return graph.breadthFirstSearchNSteps(graph.vertices[end], null) { neighbour, _ -> neighbour.elevation == 'a' }
}

private fun findStartEndIndices(input: List<String>): Pair<Int, Int> {
  var start: Int = -1
  var end: Int = -1

  for ((idx, element) in input.withIndex()) {
    start = element.indexOf('S').let { if (it != -1) normalizeCoordinate(idx, it, input[0].length) else start }
    end = element.indexOf('E').let { if (it != -1) normalizeCoordinate(idx, it, input[0].length) else end }

    if (start != -1 && end != -1) {
      break
    }
  }

  return Pair(start, end)
}

// First approach part 2.
/*private fun findAllStartIndices(input: List<String>, start: Char): List<Int> = input.flatMapIndexed { index, row ->
  Regex(start.toString()).findAll(row).map { normalizeCoordinate(index, it.range.first, input[0].length) }
}*/

private fun normalizeCoordinate(i: Int, j: Int, nCols: Int): Int = i * nCols + j

private data class Graph(val vertices: List<Vertex>) {
  // `start` and `end` must be different (not checked).
  // `found` can be replaced with `Int.MAX_VALUE` initial distance and filtering visited from the result.
  fun breadthFirstSearchNSteps(
    start: Vertex, end: Vertex? = null, foundCondition: (currentNeighbour: Vertex, end: Vertex?) -> Boolean
  ): Int {
    val queue = ArrayDeque<Vertex>().apply { add(start) }
    var found = false

    while (!queue.isEmpty()) {
      val current = queue.removeFirst().apply { visited = true }

      current.neighbours.filterNot { it.visited || it.queued }.firstOrNull {
        it.distance = current.distance + 1

        if (foundCondition(it, end)) {
          found = true
          queue.clear()
          true
        } else {
          it.queued = true
          queue.addLast(it)
          false
        }
      }
    }

    return if (found) vertices.maxOf(Vertex::distance) else Int.MAX_VALUE
  }

  companion object {
    @JvmStatic
    fun createGraph(
      input: List<String>, filterNeighbourCondition: (vertex: Vertex, neighbour: Vertex) -> Boolean
    ): Graph {
      var counter = 0
      val vertices = input.map { it.toCharArray().toList() }.map { row ->
        // `id` can be obtained with `normalizeCoordinate`.
        row.map { Vertex(counter++, if (it == 'S') 'a' else if (it == 'E') 'z' else it) }
      }

      vertices.forEachIndexed { i, row ->
        row.forEachIndexed { j, vertex ->
          vertex.neighbours.addAll(calculateNeighboursCoords(i, j, input.size, input[0].length).map {
            vertices[it.first][it.second]
          }.filter { filterNeighbourCondition(vertex, it) })
        }
      }

      return Graph(vertices.flatten())
    }

    /* Another option is to return only right and down neighbours and add source vertex as neighbour to calculated
     * neighbours. */
    private fun calculateNeighboursCoords(row: Int, col: Int, nRows: Int, nCols: Int): List<Pair<Int, Int>> = listOf(
      Pair(row - 1, col), Pair(row, col - 1), Pair(row, col + 1), Pair(row + 1, col)
    ).filter { it.first in 0 until nRows && it.second in 0 until nCols }
  }
}

private data class Vertex(val id: Int, val elevation: Char, var visited: Boolean = false, var distance: Int = 0) {
  var neighbours: MutableList<Vertex> = mutableListOf()
  var queued = false
}
