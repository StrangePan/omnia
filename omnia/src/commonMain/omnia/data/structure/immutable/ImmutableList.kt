package omnia.data.structure.immutable

import omnia.data.iterate.ArrayIterator
import omnia.data.iterate.IntegerRangeIterator
import omnia.data.iterate.MappingIterator
import omnia.data.structure.IntRange
import omnia.data.structure.List

class ImmutableList<E : Any> : List<E> {

  private val elements: Array<E>

  fun toBuilder(): Builder<E> {
    return builder<E>().addAll(this)
  }

  class Builder<E : Any> : AbstractBuilder<E, Builder<E>, ImmutableList<E>>() {

    val count get() = elements.count

    fun insertAt(index: Int, item: E): Builder<E> {
      elements.insertAt(index, item)
      return self
    }

    fun removeAt(index: Int): Builder<E> {
      elements.removeAt(index)
      return self
    }

    fun replaceAt(index: Int, item: E): Builder<E> {
      elements.replaceAt(index, item)
      return self
    }

    override fun build(): ImmutableList<E> {
      return if (elements.isPopulated) ImmutableList(this) else empty()
    }

    override val self: Builder<E>
      get() = this
  }

  private constructor() {
    // The elements array must never be accessible externally.
    @Suppress("UNCHECKED_CAST")
    this.elements = arrayOfNulls<Any>(0) as Array<E>
  }

  private constructor(builder: Builder<E>) {
    @Suppress("UNCHECKED_CAST")
    this.elements =
      Array<Any>(builder.elements.count) { builder.elements.itemAt(it) } as Array<E>
  }

  override fun itemAt(index: Int): E {
    if (index < 0 || index >= elements.size) {
      throw IndexOutOfBoundsException("$index outside the range empty [0,${elements.size})")
    }
    return elements[index]
  }

  override fun indexOf(item: Any?): Int? {
    for (i in elements.indices) {
      if (item == elements[i]) {
        return i
      }
    }
    return null
  }

  override fun iterator(): Iterator<E> {
    return ArrayIterator(elements)
  }

  override val isPopulated: Boolean get() = elements.isNotEmpty()

  override fun containsUnknownTyped(item: Any?): Boolean {
    for (element1 in elements) {
      if (item == element1) {
        return true
      }
    }
    return false
  }

  override val count get() = elements.size

  override fun equals(other: Any?): Boolean {
    if (other !is ImmutableList<*>) {
      return false
    }
    val n = count
    if (n != other.count) {
      return false
    }
    for (i in 0 until n) {
      if (itemAt(i) != other.itemAt(i)) {
        return false
      }
    }
    return true
  }

  override fun toString() = "ImmutableList(${elements.size})${elements.joinToString(",", "{", "}")}"

  override fun hashCode() = elements.contentHashCode()

  fun getSublist(intRange: IntRange): ImmutableList<E> {
    return sublistStartingAt(intRange.start).length(intRange.count)
  }

  fun sublistStartingAt(startingIndex: Int): SublistBuilder {
    return SublistBuilder(startingIndex)
  }

  inner class SublistBuilder(private val startingIndex: Int) {

    fun length(length: Int): ImmutableList<E> {
      return to(startingIndex + length)
    }

    fun to(endingIndex: Int): ImmutableList<E> {
      require(endingIndex >= startingIndex) {
        "endingIndex must be greater than or equal to startingIndex. " +
            "startingIndex=$startingIndex endingIndex=$endingIndex"
      }
      return builder<E>()
        .addAll(
          Iterable {
            MappingIterator(
              IntegerRangeIterator.create(startingIndex, endingIndex)
            ) { index: Int -> itemAt(index) }
          })
        .build()
    }
  }

  companion object {

    private val EMPTY_LIST: ImmutableList<*> = ImmutableList<Any>()

    fun <E : Any> empty(): ImmutableList<E> {
      @Suppress("UNCHECKED_CAST")
      return EMPTY_LIST as ImmutableList<E>
    }

    /**
     * Creates a new [ImmutableList] instance using the provided items as contents. At least
     * item must be provided; use [empty] to get an [ImmutableList] with no contents.
     */
    fun <E : Any> of(firstItem: E, vararg items: E): ImmutableList<E> {
      return builder<E>().add(firstItem).addAll(*items).build()
    }

    /**
     * Copies the contents of the provided iterable into a new [ImmutableList] instance. If
     * the provided iterable is already an [ImmutableList], this function returns the
     * original list without creating a copy.
     */
    fun <E : Any> copyOf(iterable: Iterable<E>): ImmutableList<E> {
      return if (iterable is ImmutableList<*>) {
        iterable as ImmutableList<E>
      } else builder<E>().addAll(iterable).build()
    }

    /** Copies the items from the provided array into a new [ImmutableList] instance.  */
    fun <E : Any> copyOf(items: Array<out E>): ImmutableList<E> {
      return builder<E>().addAll(*items).build()
    }

    fun <E : Any> Iterable<E>.toImmutableList(): ImmutableList<E> {
      return copyOf(this)
    }


    fun <E : Any> builder(): Builder<E> {
      return Builder()
    }
  }
}
