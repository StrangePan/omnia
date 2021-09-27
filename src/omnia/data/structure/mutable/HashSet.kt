package omnia.data.structure.mutable

import java.util.Objects
import java.util.function.BiPredicate
import java.util.function.Function
import java.util.function.ToIntFunction
import java.util.stream.Collectors
import java.util.stream.Stream
import omnia.data.iterate.MappingIterator
import omnia.data.structure.Collection

class HashSet<E>(
  original: Collection<out E>? = null,
  equalsFunction: BiPredicate<in Any?, in Any?>? = null,
  hashFunction: ToIntFunction<in Any>? = null,
) : MutableSet<E> {

  private val equalsFunction: BiPredicate<in Any?, in Any?> =
    equalsFunction ?: BiPredicate<Any?, Any?> { a, b -> Objects.equals(a, b) }

  private val hashFunction: ToIntFunction<in Any> =
    hashFunction ?: ToIntFunction<Any> { obj -> Objects.hashCode(obj) }

  private val kotlinSet: kotlin.collections.MutableSet<Wrapper<E>> =
    kotlin.collections.HashSet(
      (original ?: Collection.empty())
        .stream()
        .map { item -> Wrapper(item, this.equalsFunction, this.hashFunction) }
        .collect(Collectors.toSet()))

  private class Wrapper<out E>(
    private val element: E?,
    private val equalsFunction: BiPredicate<in Any?, in Any?>,
    private val hashFunction: ToIntFunction<in Any>,
  ) {

    fun element(): E? {
      return element
    }

    override fun equals(other: Any?): Boolean {
      return other is Wrapper<*> && equalsFunction.test(element, other.element)
    }

    override fun hashCode(): Int {
      return hashFunction.applyAsInt(element)
    }

    override fun toString(): String {
      return element.toString()
    }
  }

  override fun add(item: E) {
    kotlinSet.add(wrap(item))
  }

  override fun addAll(items: Collection<out E>) {
    kotlinSet.addAll(
      items.stream().map { element -> wrap(element) }.collect(Collectors.toList())
    )
  }

  override fun removeUnknownTyped(item: Any?): Boolean {
    return kotlinSet.remove(wrap(item) as Wrapper<*>)
  }

  override fun clear() {
    kotlinSet.clear()
  }

  override fun iterator(): Iterator<E> {
    return MappingIterator(kotlinSet.iterator(), unwrap())
  }

  override fun containsUnknownTyped(item: Any?): Boolean {
    return kotlinSet.contains(wrap(item) as Wrapper<*>)
  }

  override fun count(): Int {
    return kotlinSet.size
  }

  override val isPopulated: Boolean
    get() = kotlinSet.isNotEmpty()

  override fun stream(): Stream<E> {
    return kotlinSet.stream().map(unwrap())
  }

  override fun toString(): String {
    return kotlinSet.toString()
  }

  private fun <T> wrap(element: T?): Wrapper<T> {
    return Wrapper(element, equalsFunction, hashFunction)
  }

  private fun <T> unwrap(): Function<Wrapper<T>, T> {
    @Suppress("UNCHECKED_CAST")
    return UNWRAPPER_FUNCTION as Function<Wrapper<T>, T>
  }

  companion object {

    private val UNWRAPPER_FUNCTION: Function<Wrapper<*>, Any?> =
      Function<Wrapper<*>, Any?> { wrapper: Wrapper<*> -> wrapper.element() }

    @JvmStatic
    fun <E> create(): HashSet<E> {
      return HashSet()
    }

    fun <E> copyOf(original: Collection<out E>): HashSet<E> {
      return HashSet(original)
    }
  }
}