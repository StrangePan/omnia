package omnia.data.structure.immutable

import omnia.data.cache.MemoizedInt
import omnia.data.iterate.ReadOnlyIterator
import omnia.data.structure.Set
import omnia.data.structure.mutable.HashSet
import java.util.Arrays
import java.util.Objects
import java.util.function.BiPredicate
import java.util.function.ToIntFunction
import java.util.stream.Stream

class ImmutableSet<E> : Set<E> {
  private val elements: Set<E>

  fun toBuilder(): Builder<E> {
    return buildUpon(this)
  }

  class Builder<E> : AbstractBuilder<E, Builder<E>, ImmutableSet<E>>() {
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

    @kotlin.jvm.JvmStatic
    fun <T> empty(): ImmutableSet<T> {
      @Suppress("UNCHECKED_CAST")
      return EMPTY_IMMUTABLE_SET as ImmutableSet<T>
    }

    @kotlin.jvm.JvmStatic
    @SafeVarargs
    fun <E> of(firstItem: E, vararg items: E): ImmutableSet<E> {
      return builder<E>().add(firstItem).addAll(*items).build()
    }

    fun <E> copyOf(iterable: Iterable<E>): ImmutableSet<E> {
      return if (iterable is ImmutableSet<*>) {
        iterable as ImmutableSet<E>
      } else builder<E>().addAll(iterable).build()
    }

    @kotlin.jvm.JvmStatic
    fun <E> builder(): Builder<E> {
      return Builder()
    }

    fun <E> buildUpon(other: Set<out E>): Builder<E> {
      return builder<E>().addAll(other)
    }
  }
}