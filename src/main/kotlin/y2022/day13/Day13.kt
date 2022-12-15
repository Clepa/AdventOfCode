package y2022.day13

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import y2022.day13.Element.ElementInt
import y2022.day13.Element.ElementList
import kotlin.math.min

fun main() {
  val input = java.io.File("src/main/kotlin/y2022/day13/input.txt").readLines()

  // Part 1.
  println(part1(input)) // 6072.

  // Part 2.
  println(part2(input)) // 22184.
}

fun part1(input: List<String>): Int {
  val pairs = input.windowed(2, 3).map { Pair(decode(it[0]), decode(it[1])) }

  val inRightOrder = pairs.mapIndexed { idx, pair -> if (pair.first.compareTo(pair.second) == -1) idx + 1 else 0 }

  return inRightOrder.sum()
}

fun part2(input: List<String>): Int {
  val pairs = input.windowed(2, 3).flatten().apply {
    (this as MutableList)
    addAll(listOf("[[2]]", "[[6]]"))
  }.map { decode(it) }

  val sorted = pairs.sortedWith { a, b -> a.compareTo(b) }

  return (sorted.indexOf(decode("[[2]]")) + 1) * (sorted.indexOf(decode("[[6]]")) + 1)
}

private fun decode(s: String): ElementList = Json.decodeFromString(ElementListSerializer, s)

private object ElementListSerializer : KSerializer<ElementList> {
  override val descriptor: SerialDescriptor get() = buildClassSerialDescriptor("ElementListSerializer")

  override fun deserialize(decoder: Decoder): ElementList =
    mapTo((decoder as JsonDecoder).decodeJsonElement()) as ElementList

  override fun serialize(encoder: Encoder, value: ElementList) = TODO("Not implemented")

  private fun mapTo(element: JsonElement): Element = when (element) {
    is JsonArray -> ElementList(element.map(ElementListSerializer::mapTo))
    is JsonPrimitive -> ElementInt(element.int)
    else -> throw Error("""Unsupported type "${element::class}".""")
  }
}

// TODO: add Comparable<Element> and improve comparison code.
sealed class Element {
  data class ElementList(val value: List<Element>) : Element(), Iterable<Element>, Comparable<ElementList> {
    override fun iterator(): Iterator<Element> = object : Iterator<Element> {
      private val queue = ArrayDeque<ListIterator<Element>>()

      init {
        addIterator(value.listIterator())
      }

      override fun hasNext(): Boolean = queue.isNotEmpty()

      override fun next(): Element {
        val currentIterator = currentIterator()

        val next = currentIterator.next()
        if (!currentIterator.hasNext()) {
          removeIterator()
        }

        if (next is ElementList) {
          addIterator(next.value.listIterator())
        }

        return next
      }

      private fun addIterator(iterator: ListIterator<Element>) {
        if (iterator.hasNext()) {
          queue.addLast(iterator)
        }
      }

      private fun currentIterator() = queue.last()
      private fun removeIterator() = queue.removeLast()
    }

    override fun compareTo(other: ElementList): Int {
      val (leftIterator, rightIterator) = Pair(this.iterator(), other.iterator())
      val res = compare(Pair(this, other), Pair(leftIterator, rightIterator))

      return if (res != 0) res
      else if (!leftIterator.hasNext() && rightIterator.hasNext()) -1
      else if (leftIterator.hasNext() && !rightIterator.hasNext()) 1
      else throw Error("Unexpected else branch.")
    }
  }

  data class ElementInt(val value: Int) : Element(), Comparable<ElementInt> {
    override fun compareTo(other: ElementInt): Int = value.compareTo(other.value)
  }
}

private fun compare(
  pairValue: Pair<Element, Element>, pairIterator: Pair<Iterator<Element>, Iterator<Element>>
): Int {
  var res = effectiveCompare(pairValue, pairIterator)

  val (itLeft, itRight) = pairIterator
  while (res == 0 && itLeft.hasNext() && itRight.hasNext()) {
    res = effectiveCompare(Pair(itLeft.next(), itRight.next()), pairIterator)
  }

  return res
}

private fun effectiveCompare(
  pairValue: Pair<Element, Element>, pairIterator: Pair<Iterator<Element>, Iterator<Element>>
): Int {
  val (left, right) = pairValue
  return if (left is ElementInt && right is ElementInt) {
    left.compareTo(right)
  } else if (left is ElementList && right is ElementList) {
    effectiveCompareElementLists(pairValue as Pair<ElementList, ElementList>, pairIterator)
  } else if (left is ElementInt && right is ElementList) {
    compareElementIntWithElementList(left, right, pairIterator.second)
  } else if (left is ElementList && right is ElementInt) {
    -compareElementIntWithElementList(right, left, pairIterator.first)
  } else {
    throw Error("Unexpected: $pairValue.")
  }
}

private fun effectiveCompareElementLists(
  pairValue: Pair<ElementList, ElementList>, pairIterator: Pair<Iterator<Element>, Iterator<Element>>
): Int {
  val (left, right) = pairValue
  return if (left.value.isEmpty() && right.value.isNotEmpty()) {
    -1
  } else if (left.value.isNotEmpty() && right.value.isEmpty()) {
    1
  } else if (left.value.isEmpty()) {
    0
  } else {
    // Iterate over two lists if both have items.
    var res = 0
    for (i in 0 until min(left.value.size, right.value.size)) {
      val a = pairIterator.first.next()
      val b = pairIterator.second.next()

      res = if (a is ElementInt && b is ElementInt) {
        if (a == b && left.value.size != right.value.size) {
          when (i) {
            left.value.size - 1 -> -1
            right.value.size - 1 -> 1
            else -> 0
          }
        } else {
          a.compareTo(b)
        }
      } else {
        effectiveCompare(Pair(a, b), pairIterator)
      }

      if (res != 0 || a !is ElementInt || b !is ElementInt) {
        break
      }
    }
    res
  }
}

private fun compareElementIntWithElementList(
  elementLeft: ElementInt, elementRight: ElementList, elementRightIterator: Iterator<Element>
): Int {
  val left = ElementList(listOf(elementLeft))
  return compare(Pair(left, elementRight), Pair(left.iterator(), elementRightIterator))
}
