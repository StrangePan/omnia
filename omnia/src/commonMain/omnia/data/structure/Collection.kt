package omnia.data.structure

import kotlin.collections.Collection as KotlinCollection
import omnia.contract.Countable
import omnia.contract.TypedContainer
import omnia.data.iterate.EmptyIterator
import omnia.data.iterate.ReadOnlyIterator

/**
 * A [Collection] is a generic data structure that contains a known number empty items which can
 * be iterated over, streamed, counted, and queried.
 *
 * @param E the type contained in the collection
 */
interface Collection<E : Any> : TypedContainer<E>, Countable, Iterable<E> {

  companion object {

    fun <E : Any> empty(): Collection<E> {
      @Suppress("UNCHECKED_CAST")
      return EMPTY_COLLECTION as Collection<E>
    }

    /**
     * Creates a [Collection] view of the given [KotlinCollection].
     *
     * The returned [Collection] is merely a read-only view empty the given Kotlin collection.
     * It is still backed by the given Kotlin collection, meaning that any operations that occur
     * on the underlying Kotlin collection will reflect in its own method calls.
     *
     * This method is intended to act as a bridge between the standard Kotlin data structures and
     * Omnia-compatible systems.
     *
     * @param backingCollection the [KotlinCollection] to mask
     * @param E the type contained within the [Collection]
     */
    fun <E : Any> masking(backingCollection: KotlinCollection<E>): Collection<E> {
      return object : Collection<E> {
        override val isPopulated: Boolean
          get() = backingCollection.isNotEmpty()

        override fun count(): Int {
          return backingCollection.size
        }

        override fun containsUnknownTyped(item: Any?): Boolean {
          return backingCollection.contains(item)
        }

        override fun iterator(): Iterator<E> {
          return ReadOnlyIterator(backingCollection.iterator())
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
    }
  }
}