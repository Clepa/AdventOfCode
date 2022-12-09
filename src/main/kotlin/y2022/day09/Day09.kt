package y2022.day09

import kotlin.math.abs
import kotlin.math.sign

fun main() {
  val input = java.io.File("src/main/kotlin/y2022/day09/input.txt").readLines()

  // Part 1.
  println(part1(input)) // 6391.

  // Part 2.
  println(part2(input)) // 2593.
}

fun part1(input: List<String>): Int {
  val movements = input.map { line -> line.split(" ").let { Movement(Direction.fromCode(it[0]), it[1].toInt()) } }

  val rope = Rope(List(2) { Point(0, 0) })
  val grid = Grid(rope)

  movements.forEach { grid.moveRope(it) }

  return grid.tailVisited.size
}

fun part2(input: List<String>): Int {
  val movements = input.map { line -> line.split(" ").let { Movement(Direction.fromCode(it[0]), it[1].toInt()) } }

  val rope = Rope(List(10) { Point(0, 0) })
  val grid = Grid(rope)

  movements.forEach { grid.moveRope(it) }

  return grid.tailVisited.size
}

private data class Grid(val rope: Rope) {
  val tailVisited = HashSet<Point>()

  init {
    addToTailVisited(rope.tail)
  }

  fun moveRope(movement: Movement) {
    repeat(movement.units) {
      val head = rope.head
      when (movement.direction) {
        Direction.UP -> head.y += 1
        Direction.DOWN -> head.y -= 1
        Direction.RIGHT -> head.x += 1
        Direction.LEFT -> head.x -= 1
      }

      for ((leader, follower) in rope.knots.zipWithNext()) {
        val moved = moveTowardsKnot(leader, follower)
        if (!moved) {
          break
        }
      }
      addToTailVisited(rope.tail)
    }
  }

  private fun moveTowardsKnot(leader: Point, follower: Point): Boolean {
    val xDistance = leader.x - follower.x
    val yDistance = leader.y - follower.y

    return if (abs(xDistance) == 2 || abs(yDistance) == 2) {
      follower.x += xDistance.sign
      follower.y += yDistance.sign
      true
    } else {
      false
    }
  }

  private fun addToTailVisited(point: Point) {
    tailVisited.add(point.copy())
  }
}

private data class Rope(val knots: List<Point>) {
  val head: Point get() = knots.first()
  val tail: Point get() = knots.last()
}

private data class Point(var x: Int, var y: Int)
private data class Movement(val direction: Direction, val units: Int)

private enum class Direction(val code: String) {
  UP("U"), DOWN("D"), RIGHT("R"), LEFT("L");

  companion object {
    private val lookup = values().associateBy(Direction::code)
    fun fromCode(code: String): Direction =
      requireNotNull(lookup[code]) { """No Direction with code "$code". Valid values: ${Direction.lookup.keys}""" }
  }
}
