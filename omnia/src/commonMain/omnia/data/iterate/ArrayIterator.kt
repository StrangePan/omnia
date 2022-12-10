package omnia.data.iterate

import kotlin.collections.Iterator

/** A simple read-only iterator that iterates over the elements of an array in sequential order. */
class ArrayIterator<E>(private val elements: Array<E>) : Iterator<E> {

  private var i = 0

  override fun hasNext(): Boolean {
    return i < elements.size
  }

  override fun next(): E {
    if (i < elements.size) {
      return elements[i++]
    }
    throw NoSuchElementException("ArrayIterator reached last index in array: $i")
  }
}