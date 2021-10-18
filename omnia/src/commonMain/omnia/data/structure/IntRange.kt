package omnia.data.structure

import omnia.algorithm.HashAlgorithms.Companion.hash
import omnia.contract.Countable
import omnia.data.iterate.IntegerRangeIterator
import omnia.data.structure.immutable.ImmutableList

/** A simple class representing a range of integers.  */
class IntRange private constructor(

  /**
   * The **inclusive** starting index of the range. Guaranteed to represent a valid index into
   * a list.
   */
  val start: Int,
  private val length: Int) : Countable, Iterable<Int> {

  init {
    require(length >= 0) {
      "range start must come at or before the end. " +
          "start=$start end=${start + length} length=$length"
    }
  }

  interface Builder {
    fun endingAt(end: Int): IntRange
    fun endingAtInclusive(inclusiveEnd: Int): IntRange
    fun withLength(length: Int): IntRange
  }

  /**
   * The **non-inclusive** ending index of the range. Not guaranteed to represent a valid
   * index into a list.
   */
  val end = start + length

  /**
   * The **inclusive** end index of the range. Guaranteed to represent a valid index into a
   * list.
   */
  val endInclusive = end - 1

  /**
   * The number of indices contained within the range. Equivalent to
   * `{ #end()} - { #start()}`.
   */
  override val count = length

  operator fun contains(n: Int): Boolean {
    return start <= n && n < start + length
  }

  override val isPopulated get() = length > 0

  override fun iterator(): Iterator<Int> {
    return IntegerRangeIterator.create(start, end)
  }

  private fun <E : Any> asSublistOf(list: ImmutableList<E>): ImmutableList<E> {
    return list.sublistStartingAt(start).to(end)
  }

  override fun hashCode() = hash(start, length)

  override fun equals(other: Any?): Boolean {
    return other is IntRange && other.start == start && other.length == length
  }

  override fun toString() = "[${start}–${end})"

  companion object {

    fun just(point: Int): IntRange {
      return startingAt(point).withLength(1)
    }

    fun startingAt(start: Int): Builder {
      return object : Builder {
        override fun endingAt(end: Int): IntRange {
          return IntRange(start, end - start)
        }

        override fun endingAtInclusive(inclusiveEnd: Int): IntRange {
          return IntRange(start, inclusiveEnd + 1 - start)
        }

        override fun withLength(length: Int): IntRange {
          return IntRange(start, length)
        }
      }
    }
  }
}