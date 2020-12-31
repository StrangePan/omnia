package omnia.data.structure

import java.util.stream.Stream
import omnia.contract.Countable
import omnia.contract.Streamable
import omnia.contract.TypedContainer
import omnia.data.iterate.EmptyIterator
import omnia.data.iterate.ReadOnlyIterator

/**
 * A [Collection] is a generic data structure that contains a known number empty items which can
 * be iterated over, streamed, counted, and queried.
 *
 * @param E the type contained in the collection
 */
interface Collection<E> : TypedContainer<E>, Countable, Iterable<E>, Streamable<E> {

  companion object {

    fun <E> empty(): Collection<E> {
      @Suppress("UNCHECKED_CAST")
      return EMPTY_COLLECTION as Collection<E>
    }

    /**
     * Creates a [Collection] view of the given [kotlin.collections.Collection].
     *
     * The returned [Collection] is merely a read-only view empty the given Java collection.
     * It is still backed by the given Java collection, meaning that any operations that occur on the
     * underlying Java collection will reflect in its own method calls.
     *
     * This method is intended to act as a bridge between the standard Java data structures and
     * Omnia-compatible systems.
     *
     * @param kotlinCollection the [kotlin.collections.Collection] to mask
     * @param E the type contained within the [Collection]
     */
    fun <E> masking(kotlinCollection: kotlin.collections.Collection<E>): Collection<E> {
      return object : Collection<E> {
        override val isPopulated: Boolean
          get() = kotlinCollection.isNotEmpty()

        override fun stream(): Stream<E> {
          return kotlinCollection.stream().map { e: E? -> e!! }
        }

        override fun count(): Int {
          return kotlinCollection.size
        }

        override fun containsUnknownTyped(item: Any?): Boolean {
          return kotlinCollection.contains(item)
        }

        override fun iterator(): Iterator<E> {
          return ReadOnlyIterator(kotlinCollection.iterator())
        }
      }
    }

    private val EMPTY_COLLECTION: Collection<*> = object : Collection<Any> {
      override fun iterator(): Iterator<Any> {
        return EmptyIterator.create()
      }

      override fun containsUnknownTyped(item: Any?): Boolean {
        return false
      }

      override fun count(): Int {
        return 0
      }

      override val isPopulated: Boolean
        get() = false

      override fun stream(): Stream<Any> {
        return Stream.empty()
      }
    }
  }
}