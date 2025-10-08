package omnia.data.structure.immutable

import omnia.algorithm.HashAlgorithms.Companion.hash
import omnia.data.structure.ListSet

class ImmutableListSet<E: Any> private constructor(
  private val list: ImmutableList<E>,
  private val set: ImmutableSet<E>):
  ListSet<E> {

  init {
    check(list.count == set.count)
  }

  override fun containsUnknownTyped(item: Any?) = set.containsUnknownTyped(item)

  override val count: Int get() = list.count

  override fun iterator() = list.iterator()

  override fun itemAt(index: Int) = list.itemAt(index)

  override fun indexOf(item: Any?) = list.indexOf(item)

  override fun equals(other: Any?) =
    other === this
      || other is ImmutableListSet<*>
        && other.list == list
        && other.set == set

  override fun hashCode() = hash(list, set)

  override fun toString() = "ImmutableListSet(${list.count})${list.joinToString(",", "{", "}")}"

  class Builder<E : Any> : AbstractBuilder<E, Builder<E>, ImmutableListSet<E>>() {

    override fun build(): ImmutableListSet<E> = copyOf(elements)

    override val self: Builder<E> get() = this
  }

  companion object {
    private val EMPTY_LIST_SET: ImmutableListSet<*> = ImmutableListSet<Any>(ImmutableList.empty(), ImmutableSet.empty())

    @Suppress("UNCHECKED_CAST")
    fun <E: Any> empty() = EMPTY_LIST_SET as ImmutableListSet<E>

    fun <E: Any> of(firstItem: E, vararg items: E): ImmutableListSet<E> =
      builder<E>().addAll(firstItem, *items).build()

    fun <T: Any> copyOf(iterable: Iterable<T>): ImmutableListSet<T> =
      (iterable as? ImmutableListSet<T>)
        ?: iterable.distinct().let { ImmutableListSet(ImmutableList.copyOf(it), ImmutableSet.copyOf(it)) }

    fun <T: Any> builder() = Builder<T>()
  }
}

fun <E: Any> Iterable<E>.toImmutableListSet(): ImmutableListSet<E> = ImmutableListSet.copyOf(this)
