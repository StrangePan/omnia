package omnia.data.structure.mutable

import omnia.data.iterate.MappingIterator
import omnia.data.structure.Collection
import java.util.Objects
import java.util.function.BiPredicate
import java.util.function.Function
import java.util.function.ToIntFunction
import java.util.stream.Collectors
import java.util.stream.Stream

class HashSet<E>(
    original: Collection<E>? = null,
    equalsFunction: BiPredicate<in Any?, in Any?>? = null,
    hashFunction: ToIntFunction<in Any>? = null,
) : MutableSet<E> {

  private val equalsFunction: BiPredicate<in Any?, in Any?> =
      equalsFunction ?: BiPredicate<Any?, Any?> { a, b -> Objects.equals(a, b) }

  private val hashFunction: ToIntFunction<in Any> =
      hashFunction ?: ToIntFunction<Any> { obj -> Objects.hashCode(obj) }

  private val javaSet: kotlin.collections.MutableSet<Wrapper<E>> =
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
  }

  override fun add(item: E) {
    javaSet.add(wrap(item))
  }

  override fun addAll(items: Collection<out E>) {
    javaSet.addAll(
        items.stream().map { element -> wrap(element) }.collect(Collectors.toList()))
  }

  override fun removeUnknownTyped(item: Any?): Boolean {
    return javaSet.remove(wrap(item) as Wrapper<*>)
  }

  override fun clear() {
    javaSet.clear()
  }

  override fun iterator(): Iterator<E> {
    return MappingIterator(javaSet.iterator(), unwrap())
  }

  override fun containsUnknownTyped(item: Any?): Boolean {
    return javaSet.contains(wrap(item) as Wrapper<*>)
  }

  override fun count(): Int {
    return javaSet.size
  }

  override val isPopulated: Boolean
    get() = javaSet.isNotEmpty()

  override fun stream(): Stream<E> {
    return javaSet.stream().map(unwrap())
  }

  private fun <T> wrap(element: T?): Wrapper<T> {
    return Wrapper(element, equalsFunction, hashFunction)
  }

  private fun <T> unwrap(): Function<Wrapper<T>, T> {
    @Suppress("UNCHECKED_CAST")
    return UNWRAPPER_FUNCTION as Function<Wrapper<T>, T>
  }

  companion object {
    private val DEFAULT_EQUALS_FUNCTION: BiPredicate<Any?, Any?> =
        BiPredicate { a: Any?, b: Any? -> Objects.equals(a, b) }
    private val DEFAULT_HASH_FUNCTION: ToIntFunction<Any?> = ToIntFunction(Any?::hashCode)
    private val UNWRAPPER_FUNCTION: Function<Wrapper<*>, Any?> = Function<Wrapper<*>, Any?> { wrapper: Wrapper<*> -> wrapper.element() }

    @kotlin.jvm.JvmStatic
    fun <E> create(): HashSet<E> {
      return HashSet()
    }

    fun <E> copyOf(original: Collection<E>): HashSet<E> {
      return HashSet(original)
    }
  }
}