package omnia.data.iterate

/**
 * An [Iterator] that delegates to another source [Iterator], but does not support
 * any operations that would mutate the underlying data structure.
 */
class ReadOnlyIterator<E>(private val maskedIterator: Iterator<E>) : Iterator<E> {

  override fun hasNext(): Boolean {
    return maskedIterator.hasNext()
  }

  override fun next(): E {
    return maskedIterator.next()
  }
}