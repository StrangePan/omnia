package omnia.data.iterate

import java.util.function.Consumer
import java.util.function.Function

/** An [Iterator] that takes a source iterator and maps its elements to a new value.  */
class MappingIterator<T, R>(
    private val source: Iterator<T>,
    private val mapper: Function<in T, out R>,
) : Iterator<R> {

  override fun hasNext(): Boolean {
    return source.hasNext()
  }

  override fun next(): R {
    return mapper.apply(source.next())
  }

  override fun forEachRemaining(action: Consumer<in R>) {
    source.forEachRemaining { x: T -> action.accept(mapper.apply(x)) }
  }
}