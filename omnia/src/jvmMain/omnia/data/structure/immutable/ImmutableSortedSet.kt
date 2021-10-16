package omnia.data.structure.immutable

import omnia.data.structure.Collection
import omnia.data.structure.SortedSet
import omnia.data.structure.mutable.SortedArraySet

/** An immutable version of [SortedSet]. */
class ImmutableSortedSet<E : Any>
private constructor(comparator: Comparator<in E>, other: Collection<out E>) : SortedSet<E> {

  private val backingSet: SortedArraySet<E> = SortedArraySet.create(comparator)

  init {
    backingSet.addAll(other)
  }

  override fun contains(item: E): Boolean {
    return backingSet.contains(item)
  }

  override fun containsUnknownTyped(item: Any?): Boolean {
    return backingSet.containsUnknownTyped(item)
  }

  override fun count() = backingSet.count()

  override val isPopulated get() = backingSet.isPopulated

  override fun itemPreceding(other: E): E? {
    return backingSet.itemPreceding(other)
  }

  override fun itemPrecedingUnknownTyped(other: Any?): E? {
    return backingSet.itemPrecedingUnknownTyped(other)
  }

  override fun itemFollowing(other: E): E? {
    return backingSet.itemFollowing(other)
  }

  override fun itemFollowingUnknownTyped(other: Any?): E? {
    return backingSet.itemFollowingUnknownTyped(other)
  }

  override fun iterator(): Iterator<E> {
    return backingSet.iterator()
  }

  companion object {

    private val EMPTY_SET: ImmutableSortedSet<*> =
      ImmutableSortedSet({ _, _ -> 0 }, ImmutableSet.empty<Any>())

    @JvmStatic
    fun <E : Any> empty(): ImmutableSortedSet<E> {
      @Suppress("UNCHECKED_CAST")
      return EMPTY_SET as ImmutableSortedSet<E>
    }

    @JvmStatic
    fun <E : Any> copyOf(
      comparator: Comparator<in E>, other: Collection<out E>,
    ): ImmutableSortedSet<E> {
      return ImmutableSortedSet(comparator, other)
    }
  }

}