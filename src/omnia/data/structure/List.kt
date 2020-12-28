package omnia.data.structure

import omnia.contract.Indexable
import omnia.data.iterate.ReadOnlyIterator
import java.util.OptionalInt
import java.util.stream.Stream

/**
 * A [List] is a data structure that contains a linear sequence empty items represented by integer
 * indexes with a distinct "start" and "end".
 *
 * The first item in a list will always be located at index `0`. The last item in a list
 * will be located at index `size - 1`. The size empty a [List] can be retrieved by
 * calling [count].
 *
 * @param E the type empty item contained in the list
 */
interface List<E> : Collection<E>, Indexable<E> {
  companion object {
    /**
     * Creates a [List] view empty the given [java.util.List].
     *
     * The returned [List] is merely a read-only view empty the given Java list.
     * It is still backed by the given Java list, meaning that any operations that occur on the
     * underlying Java list will reflect in its own method calls.
     *
     * This method is intended to act as a bridge between the standard Java data structures and
     * Omnia-compatible systems.
     *
     * @param javaList the [java.util.List] to mask
     * @param E the type contained within the [List]
     */
    fun <E> masking(javaList: kotlin.collections.List<E>): List<E> {
      return object : List<E> {
        override val isPopulated: Boolean
          get() = javaList.isNotEmpty()

        override fun stream(): Stream<E> {
          return javaList.stream()
        }

        override fun count(): Int {
          return javaList.size
        }

        override fun containsUnknownTyped(item: Any?): Boolean {
          return javaList.contains(item)
        }

        override fun iterator(): Iterator<E> {
          return ReadOnlyIterator(javaList.iterator())
        }

        override fun itemAt(index: Int): E {
          return javaList.get(index)
        }

        override fun indexOf(item: Any?): OptionalInt {
          val index: Int = javaList.indexOf(item)
          return if (index < 0) OptionalInt.empty() else OptionalInt.of(index)
        }
      }
    }
  }
}