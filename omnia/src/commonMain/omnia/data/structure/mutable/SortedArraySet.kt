package omnia.data.structure.mutable

import omnia.algorithm.ListAlgorithms.binarySearch
import omnia.algorithm.ListAlgorithms.binarySearchOrInsert

class SortedArraySet<E : Any> private constructor(private val comparator: Comparator<in E>)
  : MutableSortedSet<E> {

  private val elements: MutableList<E> = ArrayList.create()

  override fun iterator() = elements.iterator()

  override fun add(item: E) {
    binarySearchOrInsert(elements, item, comparator)
  }

  override fun addAll(items: Iterable<E>) {
    items.forEach(::add)
  }

  override fun removeUnknownTyped(item: Any?): Boolean {
    return try {
      // if we encounter a class cast exception, we know item can't be in the list
      @Suppress("UNCHECKED_CAST")
      binarySearch(elements, item as E, comparator)?.let { elements.removeAt(it) }
      true
    } catch (_: ClassCastException) {
      false
    }
  }

  override fun clear() {
    elements.clear()
  }

  override fun containsUnknownTyped(item: Any?): Boolean {
    return try {
      // if we encounter a class cast exception, we know item can't be in the list
      @Suppress("UNCHECKED_CAST")
      binarySearch(elements, item as E, comparator) != null
    } catch (_: ClassCastException) {
      false
    }
  }

  override val count get() = elements.count

  override fun itemPreceding(other: E): E? {
    return binarySearch(elements, other, comparator)
      ?.let { it - 1 }
      ?.takeIf { it >= 0 }
      ?.let(elements::itemAt)
  }

  override fun itemPrecedingUnknownTyped(other: Any?): E? {
    return try {
      // if we encounter a class cast exception, we know item can't be in the list
      @Suppress("UNCHECKED_CAST")
      itemPreceding(other as E)
    } catch (_: ClassCastException) {
      null
    }
  }

  override fun itemFollowing(other: E): E? {
    return binarySearch(elements, other, comparator)
      ?.let { it + 1 }
      ?.takeIf { it < elements.count }
      ?.let(elements::itemAt)
  }

  override fun itemFollowingUnknownTyped(other: Any?): E? {
    return try {
      // if we encounter a class cast exception, we know item can't be in the list
      @Suppress("UNCHECKED_CAST")
      itemFollowing(other as E)
    } catch (_: ClassCastException) {
      null
    }
  }

  companion object {
    fun <E : Any> create(comparator: Comparator<in E>): SortedArraySet<E> {
      return SortedArraySet(comparator)
    }
  }
}