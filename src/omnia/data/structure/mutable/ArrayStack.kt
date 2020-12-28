package omnia.data.structure.mutable

import java.util.Optional

/** A [Stack] implementation backed by arrays.  */
class ArrayStack<E : Any> private constructor() : Stack<E> {
  private val list: MutableList<E> = ArrayList.create()

  override fun push(item: E) {
    list.add(item)
  }

  override fun pop(): Optional<E> {
    return if (list.isPopulated) Optional.of(list.removeAt(list.count() - 1)) else Optional.empty()
  }

  override fun peek(): Optional<E> {
    return if (list.isPopulated) Optional.of(list.itemAt(list.count() - 1)) else Optional.empty()
  }

  override fun iterator(): Iterator<E> {
    return list.iterator()
  }

  override fun count(): Int {
    return list.count()
  }

  companion object {
    fun <E : Any> create(): Stack<E> {
      return ArrayStack()
    }
  }
}