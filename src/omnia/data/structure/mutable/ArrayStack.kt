package omnia.data.structure.mutable

/** A [Stack] implementation backed by arrays.  */
class ArrayStack<E : Any> private constructor() : Stack<E> {

  private val list: MutableList<E> = ArrayList.create()

  override fun push(item: E) {
    list.add(item)
  }

  override fun pop(): E? {
    return if (list.isPopulated) list.removeAt(list.count() - 1) else null
  }

  override fun peek(): E? {
    return if (list.isPopulated) list.itemAt(list.count() - 1) else null
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