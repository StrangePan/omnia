package omnia.data.structure.immutable

import omnia.data.structure.Collection
import omnia.data.structure.SortedSet
import omnia.data.structure.mutable.SortedArraySet

/** An immutable version of [SortedSet]. */
class ImmutableSortedSet<E : Any>
private constructor(comparator: Comparator<in E>, other: Collection<out E>) : SortedSet<E> {

  private val baseSet: SortedArraySet<E> = SortedArraySet.create(comparator)

  init {
    baseSet.addAll(other)
  }

  override fun contains(item: E): Boolean {
    return baseSet.contains(item)
  }

  override fun containsUnknownTyped(item: Any?): Boolean {
    return baseSet.containsUnknownTyped(item)
  }

  override fun count() = baseSet.count()

  override val isPopulated get() = baseSet.isPopulated

  override fun itemPreceding(other: E): E? {
    return baseSet.itemPreceding(other)
  }

  override fun itemPrecedingUnknownTyped(other: Any?): E? {
    return baseSet.itemPrecedingUnknownTyped(other)
  }

  override fun itemFollowing(other: E): E? {
    return baseSet.itemFollowing(other)
  }

  override fun itemFollowingUnknownTyped(other: Any?): E? {
    return baseSet.itemFollowingUnknownTyped(other)
  }

  override fun iterator(): Iterator<E> {
    return baseSet.iterator()
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