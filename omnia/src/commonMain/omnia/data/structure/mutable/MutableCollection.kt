package omnia.data.structure.mutable

import kotlin.collections.MutableCollection as KotlinMutableCollection
import omnia.contract.Clearable
import omnia.data.structure.Collection

/**
 * A [MutableCollection] is a [Collection] whose contents can be manipulated at
 * runtime.
 */
interface MutableCollection<E : Any> : Collection<E>, Clearable {

  /**
   * Adds the given the item to this collection. Each specific data structure has its own
   * definition of how the items are added and the semantic location the items added.
   *
   * @param item the item to add to the collection
   */
  fun add(item: E)

  /**
   * Adds all items in the given collection to this one. Each specific data structure has its own
   * definition of how the items are added and the semantic location the items added.
   *
   * @param items the items to add to the collection
   */
  fun addAll(items: Iterable<E>)

  /**
   * Removes the given item from this [Collection] if contained within.
   *
   * For collections that contain duplicates of an item, this method removes at most one duplicate
   * of that item. Callers wishing to remove all such items will need to call this function multiple
   * times.
   *
   * @param item the item to remove from the [Collection]
   * @return `true` if the collection contained [item] and the item was removed,
   * `false` if the collection did not contain the item and thus was not removed
   */
  fun remove(item: E): Boolean {
    return removeUnknownTyped(item)
  }

  /**
   * Removes the given item from this [Collection] if contained within.
   *
   * For collections that contain duplicates of an item, this method removes at most one duplicate
   * of that item. Callers wishing to remove all such items will need to call this function multiple
   * times.
   *
   * @param item the item to remove from the [Collection]
   * @return `true` if the collection contained [item] and the item was removed,
   * `false` if the collection did not contain the item and thus was not removed
   */
  fun removeUnknownTyped(item: Any?): Boolean

  /** Atomically removes all items from this [Collection].  */
  override fun clear()

  companion object {

    fun <E : Any> masking(backingCollection: KotlinMutableCollection<E>):
        MutableCollection<E> {
      return object : MutableCollection<E> {
        override fun add(item: E) {
          backingCollection.add(item)
        }

        override fun addAll(items: Iterable<E>) {
          backingCollection.addAll(items)
        }

        override fun removeUnknownTyped(item: Any?): Boolean {
          return backingCollection.remove(item)
        }

        override fun clear() {
          backingCollection.clear()
        }

        override val count: Int get() {
          return backingCollection.size
        }

        override val isPopulated: Boolean
          get() = !backingCollection.isEmpty()

        override fun containsUnknownTyped(item: Any?): Boolean {
          return backingCollection.contains(item)
        }

        override fun iterator(): MutableIterator<E> {
          return backingCollection.iterator()
        }
      }
    }
  }
}