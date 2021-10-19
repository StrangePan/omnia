package omnia.data.iterate

class EmptyIterator<out E> private constructor() : Iterator<E> {

  override fun hasNext(): Boolean {
    return false
  }

  override fun next(): E {
    throw NoSuchElementException("iterator is empty")
  }

  companion object {

    private val EMPTY_ITERATOR: EmptyIterator<*> = EmptyIterator<Any>()

    fun <E> create(): EmptyIterator<E> {
      @Suppress("UNCHECKED_CAST")
      return EMPTY_ITERATOR as EmptyIterator<E>
    }
  }
}