package y2022.day13

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import kotlin.math.min

fun main() {
  val input = java.io.File("src/main/kotlin/y2022/day13/input.txt").readLines()

  // Part 1.
  val p1 = part1(input)
  println(p1) // 6072.
  if (p1 != 6072) throw Error("First part wrong result.") // TODO: delete.

  // Part 2.
  val p2 = part2(input)
  println(p2) // 22184.
  if (p2 != 22184) throw Error("Second part wrong result.") // TODO: delete.
}

fun part1(input: List<String>): Int {
  val pairs = input.windowed(2, 3).map { window -> Pair(decode(window[0]), decode(window[1])) }

  //val inRightOrder = pairs.subList(83,84).mapIndexed { idx, pair ->
  val inRightOrder = pairs.mapIndexed { idx, pair ->
    println("--- ${idx + 1} pair ---\n\t${pair.first}\n\t${pair.second}")

    val res = isPairInRightOrder(pair)

    println(">>> $res")

    if (res) idx + 1 else 0
  }

  println("Final:\n\t$inRightOrder")

  return inRightOrder.sum()
}

fun part2(input: List<String>): Int {
  val pairs = input.windowed(2, 3).flatten().apply {
    (this as MutableList)
    add("[[2]]")
    add("[[6]]")
  }.map { decode(it) }.sortedWith { o1, o2 -> comparePart2(Pair(o1, o2)) }

  return (pairs.indexOf(decode("[[2]]")) + 1) * (pairs.indexOf(decode("[[6]]")) + 1)
}

private fun isPairInRightOrder(pair: Pair<List<Any>, List<Any>>): Boolean = start(pair) == OrderStatus.CORRECT_ORDER

private fun comparePart2(pair: Pair<List<Any>, List<Any>>): Int {
  return when (start(pair)) {
    OrderStatus.CORRECT_ORDER -> -1
    OrderStatus.INCORRECT_ORDER -> 1
    OrderStatus.CONTINUE -> 0
  }
}

private fun start(pair: Pair<List<Any>, List<Any>>): OrderStatus {
  println("\t>>> start")

  val leftIterator = AnyListIterable(pair.first).iterator()
  val rightIterator = AnyListIterable(pair.second).iterator()
  var leftValue: Any
  var rightValue: Any

  if (!leftIterator.hasNext() || !rightIterator.hasNext()) {
    leftValue = pair.first
    rightValue = pair.second
  } else {
    leftValue = leftIterator.next()
    rightValue = rightIterator.next()
  }

  println("\tInit: $leftValue ********* $rightValue ")

  var res = compare(leftIterator, rightIterator, leftValue, rightValue)

  if (res == OrderStatus.CONTINUE) {
    if (leftIterator.hasNext() && !rightIterator.hasNext()) {
      res = OrderStatus.INCORRECT_ORDER
    } else if (!leftIterator.hasNext() && rightIterator.hasNext()) {
      res = OrderStatus.CORRECT_ORDER
    }
  }

  println("\tHasNexts: ${leftIterator.hasNext()} ### ${rightIterator.hasNext()}")
  return res
}

private fun compare(
  leftIterator: Iterator<Any>, rightIterator: Iterator<Any>, leftValue: Any, rightValue: Any
): OrderStatus {
  println("\t\tRecursive $leftValue ********* $rightValue")
  var res: OrderStatus = effectiveCompare(Pair(leftIterator, rightIterator), Pair(leftValue, rightValue))

  while (leftIterator.hasNext() && rightIterator.hasNext() && res == OrderStatus.CONTINUE) {
    val leftValue = leftIterator.next()
    val rightValue = rightIterator.next()

    println("\t\tWhile: $leftValue  ********* $rightValue")
    res = effectiveCompare(Pair(leftIterator, rightIterator), Pair(leftValue, rightValue))
  }

  return res
}

private fun effectiveCompare(pairIterator: Pair<Iterator<Any>, Iterator<Any>>, pair: Pair<Any, Any>): OrderStatus {
  println("\t\t\tMethod compare: $pair")
  val (left, right) = pair
  val x = if (left is Int && right is Int) {
    if (left < right) OrderStatus.CORRECT_ORDER else if (left > right) OrderStatus.INCORRECT_ORDER else OrderStatus.CONTINUE
  } else if (left is List<*> && right is List<*>) {
    if (left.isEmpty() && right.isNotEmpty()) {
      OrderStatus.CORRECT_ORDER
    } else if (left.isNotEmpty() && right.isEmpty()) {
      OrderStatus.INCORRECT_ORDER
    } else if (left.isEmpty() && right.isEmpty()) {
      OrderStatus.CONTINUE
    } else if (left.isNotEmpty() && right.isNotEmpty()) {
      println("\t\t\t\tNotEmptyLists")
      var i = 0
      var res = OrderStatus.CONTINUE
      while (i < min(left.size, right.size) && res == OrderStatus.CONTINUE) {
        println("\t\t\t\t... i: $i")
        if (left[i] is Int && right[i] is Int) {
          val a = left[i] as Int
          val b = right[i] as Int
          res = if (a == b && left.size != right.size) {
            println("\t\t\t\tDifferent sizes")
            when (i) {
              left.size - 1 -> OrderStatus.CORRECT_ORDER
              right.size - 1 -> OrderStatus.INCORRECT_ORDER
              else -> OrderStatus.CONTINUE
            }
          } else if (a < b) {
            OrderStatus.CORRECT_ORDER
          } else if (a > b) {
            OrderStatus.INCORRECT_ORDER
          } else {
            OrderStatus.CONTINUE
          }
          println("\t\t\t\tPartial result: $res")
        } else {
          break
        }
        i++
      }
      println("\t\t\t\tresult: $res")
      res
    } else {
      throw Error("Unreachable code.")
    }
  } else if ((left is Int && right is List<*>) || (left is List<*> && right is Int)) {
    val l = if (left is Int) listOf(left) else left
    val r = if (right is Int) listOf(right) else right
    val itL = if (left is Int) AnyListIterable(l as List<Any>).iterator() else pairIterator.first
    val itR = if (right is Int) AnyListIterable(r as List<Any>).iterator() else pairIterator.second
    compare(itL, itR, l, r)
  } else {
    throw Error("Unexpected: $pair")
  }
  println("\t\t\tMethod compare result: $x")
  return x
}

private fun mapIntToOrderStatus(value: Int) = when (value) {
  -1 -> OrderStatus.INCORRECT_ORDER
  1 -> OrderStatus.CORRECT_ORDER
  else -> throw Error("Wrong OrderStatus")
}

private fun decode(s: String): List<Any> = Json.decodeFromString(InputSerializer, s) as List<Any>

private object InputSerializer : KSerializer<Any> {
  override val descriptor: SerialDescriptor get() = buildClassSerialDescriptor("InputSerializer")

  override fun deserialize(decoder: Decoder): Any {
    require(decoder is JsonDecoder)
    return mapTo(decoder.decodeJsonElement())
  }

  override fun serialize(encoder: Encoder, value: Any) = TODO("Not yet implemented")

  private fun mapTo(element: Any): Any {
    return when (element) {
      is JsonArray -> element.map { mapTo(it) }
      is JsonPrimitive -> element.int
      else -> throw Error("""Unsupported type "${element::class}"""")
    }
  }
}

// `list` can have elements of any type: numbers, strings, other lists,... All together.
private class AnyListIterable(private val list: List<Any>) : Iterable<Any> {
  override fun iterator() = object : Iterator<Any> {
    private val queue = ArrayDeque<ListIterator<Any>>()

    init {
      queue.add(list.listIterator())
    }

    override fun hasNext(): Boolean = queue.isNotEmpty() && currentIterator().hasNext() // TODO: is this condition
    // correct?

    override fun next(): Any {
      val iterator = currentIterator()
      val next = if (iterator.hasNext()) iterator.next() else throw Error("There are no more items.")

      //if (!iterator.hasNext()) {
      //  removeIterator()
      //}

      if (next is List<*>) {
        addIterator(next.listIterator() as ListIterator<Any>)
      }
      while (queue.isNotEmpty() && !currentIterator().hasNext()) {
        removeIterator()
      }

      return next
    }

    private fun addIterator(iterator: ListIterator<Any>) = queue.addLast(iterator)
    private fun currentIterator() = queue.last()
    private fun removeIterator() = queue.removeLast()
  }
}

private enum class OrderStatus { CORRECT_ORDER, INCORRECT_ORDER, CONTINUE }
