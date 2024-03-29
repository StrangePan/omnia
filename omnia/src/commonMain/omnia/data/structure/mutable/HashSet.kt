package omnia.data.structure.mutable

import kotlin.collections.HashSet as KotlinHashSet
import omnia.data.iterate.MappingIterator
import omnia.data.structure.Collection

class HashSet<E : Any>(
  original: Iterable<E>? = null,
  equalsFunction: ((Any?, Any?) -> Boolean)? = null,
  hashFunction: ((Any) -> Int)? = null,
) : MutableSet<E> {

  private val equalsFunction: (Any?, Any?) -> Boolean =
    equalsFunction ?: { a, b -> a == b }

  private val hashFunction: (Any) -> Int = hashFunction ?: Any::hashCode

  private val backingSet =
    KotlinHashSet(
      (original ?: Collection.empty())
        .map { item -> Wrapper(item, this.equalsFunction, this.hashFunction) })

  private class Wrapper<out E : Any>(
    private val element: E,
    private val equalsFunction: (Any?, Any?) -> Boolean,
    private val hashFunction: (Any) -> Int,
  ) {

    fun element(): E {
      return element
    }

    override fun equals(other: Any?): Boolean {
      return other is Wrapper<*> && equalsFunction(element, other.element)
    }

    override fun hashCode(): Int {
      return hashFunction(element)
    }

    override fun toString(): String {
      return element.toString()
    }
  }

  override fun add(item: E) {
    backingSet.add(wrap(item))
  }

  override fun addAll(items: Iterable<E>) {
    backingSet.addAll(items.map { element -> wrap(element) })
  }

  override fun removeUnknownTyped(item: Any?): Boolean {
    return if (item != null) backingSet.remove(wrap(item) as Wrapper<*>) else false
  }

  override fun clear() {
    backingSet.clear()
  }

  override fun iterator(): Iterator<E> {
    return MappingIterator(backingSet.iterator(), unwrap())
  }

  override fun containsUnknownTyped(item: Any?): Boolean {
    return if (item != null) backingSet.contains(wrap(item) as Wrapper<*>) else false
  }

  override val count: Int get() {
    return backingSet.size
  }

  override val isPopulated: Boolean
    get() = backingSet.isNotEmpty()

  override fun toString(): String {
    return backingSet.toString()
  }

  private fun <T : Any> wrap(element: T): Wrapper<T> {
    return Wrapper(element, equalsFunction, hashFunction)
  }

  private fun <T : Any> unwrap(): (Wrapper<T>) -> T {
    @Suppress("UNCHECKED_CAST")
    return UNWRAPPER_FUNCTION as (Wrapper<T>) -> T
  }

  companion object {

    private val UNWRAPPER_FUNCTION: (Wrapper<*>) -> Any? = { it.element() }

    fun <E : Any> create(): HashSet<E> {
      return HashSet()
    }

    fun <E : Any> copyOf(original: Iterable<E>): HashSet<E> {
      return HashSet(original)
    }

    fun <E : Any> Iterable<E>.toHashSet(): HashSet<E> {
      return copyOf(this)
    }
  }
}