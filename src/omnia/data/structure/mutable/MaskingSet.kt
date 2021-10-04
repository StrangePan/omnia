package omnia.data.structure.mutable

import omnia.data.structure.Collection

open class MaskingSet<E : Any, J : kotlin.collections.MutableSet<E>>(private val kotlinSet: J) :
  MutableSet<E> {

  protected fun javaSet(): J {
    return kotlinSet
  }

  override fun add(item: E) {
    kotlinSet.add(item)
  }

  override fun addAll(items: Collection<out E>) {
    kotlinSet.addAll(items)
  }

  override fun removeUnknownTyped(item: Any?): Boolean {
    return kotlinSet.remove(item)
  }

  override fun clear() {
    kotlinSet.clear()
  }

  override fun iterator(): Iterator<E> {
    return kotlinSet.iterator()
  }

  override fun containsUnknownTyped(item: Any?): Boolean {
    return kotlinSet.contains(item)
  }

  override val isPopulated: Boolean
    get() = kotlinSet.isNotEmpty()

  override fun count(): Int {
    return kotlinSet.size
  }
}