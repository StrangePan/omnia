package omnia.data.structure.immutable

import java.util.Arrays
import java.util.Objects
import java.util.function.BiPredicate
import java.util.function.ToIntFunction
import java.util.stream.Stream
import omnia.data.cache.MemoizedInt
import omnia.data.iterate.ReadOnlyIterator
import omnia.data.structure.Set
import omnia.data.structure.mutable.HashSet

class ImmutableSet<E : Any> : Set<E> {

  private val elements: Set<E>

  fun toBuilder(): Builder<E> {
    return buildUpon(this)
  }

  class Builder<E : Any> : AbstractBuilder<E, Builder<E>, ImmutableSet<E>>() {

    var equalsFunction: BiPredicate<in Any?, in Any?>? = null
    var hashFunction: ToIntFunction<in Any>? = null

    fun equalsFunction(equalsFunction: BiPredicate<in Any?, in Any?>): Builder<E> {
      this.equalsFunction = equalsFunction
      return self
    }

    fun hashFunction(hashFunction: ToIntFunction<in Any>): Builder<E> {
      this.hashFunction = hashFunction
      return self
    }

    override fun build(): ImmutableSet<E> {
      return if (elements.isPopulated) ImmutableSet(this) else empty()
    }

    override val self: Builder<E>
      get() = this
  }

  private constructor() {
    elements = Set.empty()
  }

  private constructor(builder: Builder<E>) {
    elements = HashSet(builder.elements, builder.equalsFunction, builder.hashFunction)
  }

  override fun iterator(): Iterator<E> {
    return ReadOnlyIterator(elements.iterator())
  }

  override fun containsUnknownTyped(item: Any?): Boolean {
    return elements.containsUnknownTyped(item)
  }

  override val isPopulated: Boolean
    get() = elements.isPopulated

  override fun count(): Int {
    return elements.count()
  }

  override fun stream(): Stream<E> {
    return elements.stream()
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) {
      return true
    }
    if (other !is ImmutableSet<*>) {
      return false
    }
    if (other.count() != count()) {
      return false
    }
    for (element in elements) {
      if (!other.containsUnknownTyped(element)) {
        return false
      }
    }
    return true
  }

  override fun hashCode(): Int {
    return hashCode.value()
  }

  override fun toString(): String {
    return elements.toString()
  }

  private val hashCode: MemoizedInt = MemoizedInt.memoize { computeHash() }

  private fun computeHash(): Int {
    val elementCodes = IntArray(count())
    var i = 0
    for (element in elements) {
      elementCodes[i++] = element.hashCode()
    }
    Arrays.sort(elementCodes)
    return Objects.hash(elementCodes.contentHashCode())
  }

  companion object {

    private val EMPTY_IMMUTABLE_SET: ImmutableSet<*> = ImmutableSet<Any>()

    @JvmStatic
    fun <E : Any> empty(): ImmutableSet<E> {
      @Suppress("UNCHECKED_CAST")
      return EMPTY_IMMUTABLE_SET as ImmutableSet<E>
    }

    @JvmStatic
    @SafeVarargs
    fun <E : Any> of(firstItem: E, vararg items: E): ImmutableSet<E> {
      return builder<E>().add(firstItem).addAll(*items).build()
    }

    @JvmStatic
    fun <E : Any> copyOf(iterable: Iterable<E>): ImmutableSet<E> {
      return if (iterable is ImmutableSet<*>) {
        iterable as ImmutableSet<E>
      } else builder<E>().addAll(iterable).build()
    }

    @JvmStatic
    fun <E : Any> builder(): Builder<E> {
      return Builder()
    }

    @JvmStatic
    fun <E : Any> buildUpon(other: Set<out E>): Builder<E> {
      return builder<E>().addAll(other)
    }
  }
}