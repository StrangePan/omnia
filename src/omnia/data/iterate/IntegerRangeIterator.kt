package omnia.data.iterate

class IntegerRangeIterator private constructor(
  private var next: Int,
  private val end: Int,
  private val increment: Int
) : Iterator<Int> {

  override fun hasNext(): Boolean {
    return if (increment > 0) next + increment < end else next + increment > end
  }

  override fun next(): Int {
    val current = next
    next += increment
    return current
  }

  companion object {

    fun create(start: Int, end: Int): IntegerRangeIterator {
      return IntegerRangeIterator(start, end, if (start <= end) 1 else -1)
    }
  }
}