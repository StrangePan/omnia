package omnia.data.structure


import omnia.contract.Indexable
import omnia.data.iterate.ReadOnlyIterator

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
interface List<E : Any> : Collection<E>, Indexable<E> {

  companion object {

    /**
     * Creates a [List] view empty the given [kotlin.collections.List].
     *
     * The returned [List] is merely a read-only view empty the given Kotlin list.
     * It is still backed by the given Kotlin list, meaning that any operations that occur on the
     * underlying Kotlin list will reflect in its own method calls.
     *
     * This method is intended to act as a bridge between the standard Kotlin data structures and
     * Omnia-compatible systems.
     *
     * @param backingList the [kotlin.collections.List] to mask
     * @param E the type contained within the [List]
     */
    fun <E : Any> masking(backingList: kotlin.collections.List<E>): List<E> {
      return object : List<E> {
        override val isPopulated: Boolean
          get() = backingList.isNotEmpty()

        override fun count(): Int {
          return backingList.size
        }

        override fun containsUnknownTyped(item: Any?): Boolean {
          return backingList.contains(item)
        }

        override fun iterator(): Iterator<E> {
          return ReadOnlyIterator(backingList.iterator())
        }

        override fun itemAt(index: Int): E {
          return backingList[index]
        }

        override fun indexOf(item: Any?): Int? {
          val index = backingList.indexOf(item)
          return if (index < 0) null else index
        }
      }
    }
  }
}