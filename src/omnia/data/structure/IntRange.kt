package omnia.data.structure

import omnia.algorithm.HashAlgorithms.Companion.hash
import omnia.contract.Countable
import omnia.data.iterate.IntegerRangeIterator
import omnia.data.structure.immutable.ImmutableList

/** A simple class representing a range of integers.  */
class IntRange private constructor(start: Int, length: Int) : Countable, Iterable<Int> {

  private val start: Int
  private val length: Int

  interface Builder {
    fun endingAt(end: Int): IntRange
    fun endingAtInclusive(inclusiveEnd: Int): IntRange
    fun withLength(length: Int): IntRange
  }

  /**
   * The **inclusive** starting index of the range. Guaranteed to represent a valid index into
   * a list.
   */
  fun start(): Int {
    return start
  }

  /**
   * The **non-inclusive** ending index of the range. Not guaranteed to represent a valid
   * index into a list.
   */
  fun end(): Int {
    return start + length
  }

  /**
   * The **inclusive** end index of the range. Guaranteed to represent a valid index into a
   * list.
   */
  fun endInclusive(): Int {
    return end() - 1
  }

  /**
   * The number of indices contained within the range. Equivalent to
   * `{ #end()} - { #start()}`.
   */
  override fun count(): Int {
    return length
  }

  operator fun contains(n: Int): Boolean {
    return start <= n && n < start + length
  }

  override val isPopulated get() = length > 0

  override fun iterator(): Iterator<Int> {
    return IntegerRangeIterator.create(start, end())
  }

  private fun <E : Any> asSublistOf(list: ImmutableList<E>): ImmutableList<E> {
    return list.sublistStartingAt(start()).to(end())
  }

  override fun hashCode() = hash(start, length)

  override fun equals(other: Any?): Boolean {
    return other is IntRange && other.start == start && other.length == length
  }

  override fun toString() = "[${start()}–${end()})"

  companion object {

    @JvmStatic
    fun just(point: Int): IntRange {
      return startingAt(point).withLength(1)
    }

    @JvmStatic
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

  init {
    require(length >= 0) {
      String.format(
        "range start must come at or before the end. start=%d end=%d length=%d",
        start,
        start + length,
        length
      )
    }
    this.start = start
    this.length = length
  }
}