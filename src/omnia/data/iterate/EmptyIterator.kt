package omnia.data.iterate

import java.util.Objects
import java.util.function.Consumer

class EmptyIterator<out E> private constructor() : Iterator<E> {
  override fun hasNext(): Boolean {
    return false
  }

  override fun next(): E {
    throw NoSuchElementException("iterator is empty")
  }

  override fun forEachRemaining(action: Consumer<in E>) {
    Objects.requireNonNull(action)
  }

  companion object {
    private val EMPTY_ITERATOR: EmptyIterator<*> = EmptyIterator<Any>()

    @kotlin.jvm.JvmStatic
    fun <E> create(): EmptyIterator<E> {
      @Suppress("UNCHECKED_CAST")
      return EMPTY_ITERATOR as EmptyIterator<E>
    }
  }
}