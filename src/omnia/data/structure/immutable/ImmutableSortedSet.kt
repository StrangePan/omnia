package omnia.data.structure.immutable

import java.util.Optional
import java.util.stream.Stream
import omnia.data.structure.Collection
import omnia.data.structure.SortedSet
import omnia.data.structure.mutable.TreeSet

/** An immutable version of [SortedSet].  */
class ImmutableSortedSet<E> private constructor(
  comparator: Comparator<in E>,
  other: Collection<out E>
) : SortedSet<E> {

  private val baseSet: TreeSet<E> = TreeSet.create(comparator)

  override fun contains(item: E): Boolean {
    return baseSet.contains(item)
  }

  /**
   * @inheritDoc
   *
   * **Note:** this type-unsafe version of [contains] is inherently less performant
   * because we cannot perform a binary search on objects of unknown types.
   *
   * @param item the element to search for
   * @return true if the set contains the element, false if not
   */
  override fun containsUnknownTyped(item: Any?): Boolean {
    return baseSet.containsUnknownTyped(item)
  }

  override fun count(): Int {
    return baseSet.count()
  }

  override val isPopulated: Boolean
    get() = baseSet.isPopulated

  override fun itemPreceding(other: E): Optional<E> {
    return baseSet.itemPreceding(other)
  }

  override fun itemFollowing(other: E): Optional<E> {
    return baseSet.itemFollowing(other)
  }

  override fun iterator(): Iterator<E> {
    return baseSet.iterator()
  }

  override fun stream(): Stream<E> {
    return baseSet.stream()
  }

  companion object {

    private val EMPTY_SET: ImmutableSortedSet<*> =
      ImmutableSortedSet({ _, _ -> 0 }, ImmutableSet.empty<Any>())

    @JvmStatic
    fun <E> empty(): ImmutableSortedSet<E> {
      @Suppress("UNCHECKED_CAST")
      return EMPTY_SET as ImmutableSortedSet<E>
    }

    @JvmStatic
    fun <E> copyOf(
      comparator: Comparator<in E>, other: Collection<out E>,
    ): ImmutableSortedSet<E> {
      return ImmutableSortedSet(comparator, other)
    }
  }

  init {
    baseSet.addAll(other)
  }
}