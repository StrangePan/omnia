package omnia.data.structure.mutable

/** A [Stack] implementation backed by arrays.  */
class ArrayStack<E : Any> private constructor() : Stack<E> {

  private val backingList: MutableList<E> = ArrayList.create()

  override fun push(item: E) {
    backingList.add(item)
  }

  override fun pop(): E? {
    return if (backingList.isPopulated) backingList.removeAt(backingList.count() - 1) else null
  }

  override fun peek(): E? {
    return if (backingList.isPopulated) backingList.itemAt(backingList.count() - 1) else null
  }

  override fun iterator(): Iterator<E> {
    return backingList.iterator()
  }

  override fun count(): Int {
    return backingList.count()
  }

  companion object {

    fun <E : Any> create(): Stack<E> {
      return ArrayStack()
    }
  }
}