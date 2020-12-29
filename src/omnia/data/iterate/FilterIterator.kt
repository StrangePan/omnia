package omnia.data.iterate

import java.util.NoSuchElementException
import java.util.function.Predicate

class FilterIterator<E>(private val source: Iterator<E>, private val filter: Predicate<in E>) :
  Iterator<E> {

  private var didPeekNext = false
  private var peekedNext: E? = null
  private var hasNext = false

  override fun hasNext(): Boolean {
    maybePeekNext()
    return hasNext
  }

  override fun next(): E {
    maybePeekNext()
    if (!hasNext) {
      throw NoSuchElementException("invoked next() when there is no next")
    }
    return clearNext()
  }

  private fun maybePeekNext() {
    if (didPeekNext) {
      return
    }
    didPeekNext = true
    hasNext = false
    while (source.hasNext()) {
      val next = source.next()
      peekedNext = next
      if (filter.test(next)) {
        hasNext = true
        break
      }
    }
  }

  private fun clearNext(): E {
    didPeekNext = false
    val next = peekedNext!!
    peekedNext = null
    return next
  }
}