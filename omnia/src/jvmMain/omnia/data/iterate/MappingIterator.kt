package omnia.data.iterate


/** An [Iterator] that takes a source iterator and maps its elements to a new value.  */
class MappingIterator<T, R>(
    private val source: Iterator<T>,
    private val mapper: (T) -> R,
) : Iterator<R> {

  override fun hasNext(): Boolean {
    return source.hasNext()
  }

  override fun next(): R {
    return mapper(source.next())
  }
}