package omnia.data.structure.mutable

import kotlin.collections.MutableSet as KotlinMutableSet

open class MaskingSet<E : Any, J : KotlinMutableSet<E>>(private val backingSet: J) :
  MutableSet<E> {

  override fun add(item: E) {
    backingSet.add(item)
  }

  override fun addAll(items: Iterable<E>) {
    backingSet.addAll(items)
  }

  override fun removeUnknownTyped(item: Any?): Boolean {
    return backingSet.remove(item)
  }

  override fun clear() {
    backingSet.clear()
  }

  override fun iterator(): Iterator<E> {
    return backingSet.iterator()
  }

  override fun containsUnknownTyped(item: Any?): Boolean {
    return backingSet.contains(item)
  }

  override val isPopulated: Boolean
    get() = backingSet.isNotEmpty()

  override fun count(): Int {
    return backingSet.size
  }
}