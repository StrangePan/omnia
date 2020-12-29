package omnia.data.structure.mutable

import java.util.Comparator
import java.util.Objects
import java.util.Optional

class TreeSet<E> private constructor(comparator: Comparator<in E>) :
  MaskingSet<E, java.util.TreeSet<E>>(java.util.TreeSet<E>(comparator)), MutableSortedSet<E> {

  override fun itemPreceding(other: E): Optional<E> {
    return Optional.ofNullable(javaSet().higher(other))
  }

  override fun itemFollowing(other: E): Optional<E> {
    return Optional.ofNullable(javaSet().lower(other))
  }

  companion object {

    fun <E> create(comparator: Comparator<in E>): TreeSet<E> {
      Objects.requireNonNull(comparator)
      return TreeSet(comparator)
    }
  }
}