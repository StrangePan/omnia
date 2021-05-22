package omnia.data.structure.mutable

import java.util.Comparator
import java.util.Objects

class TreeSet<E> private constructor(comparator: Comparator<in E>) :
  MaskingSet<E, java.util.TreeSet<E>>(java.util.TreeSet<E>(comparator)), MutableSortedSet<E> {

  override fun containsUnknownTyped(item: Any?): Boolean {
    return try {
      super.containsUnknownTyped(item)
    } catch (e: ClassCastException) {
      false
    }
  }

  override fun itemPreceding(other: E): E? {
    return javaSet().higher(other)
  }

  @Suppress("UNCHECKED_CAST")
  override fun itemPrecedingUnknownTyped(other: Any?): E? {
    // Only conduct cast if already contained in set, which assumes it's compatible with the type E
    return try {
      itemPreceding(other as E)
    } catch (e: ClassCastException) {
      null
    }
  }

  override fun itemFollowing(other: E): E? {
    return javaSet().lower(other)
  }

  @Suppress("UNCHECKED_CAST")
  override fun itemFollowingUnknownTyped(other: Any?): E? {
    // Only conduct cast if already contained in set, which assumes it's compatible with the type E
    return try {
      itemFollowing(other as E)
    } catch (e: ClassCastException) {
      null
    }
  }

  companion object {
    fun <E> create(comparator: Comparator<in E>): TreeSet<E> {
      Objects.requireNonNull(comparator)
      return TreeSet(comparator)
    }
  }
}