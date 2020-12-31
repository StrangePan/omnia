package omnia.data.iterate

abstract class WrapperIterator<E> protected constructor(private val baseIterator: MutableIterator<E>) :
    MutableIterator<E> {

  private var hasCurrent = false
  private var current: E? = null

  protected fun current(): E? {
    check(hasCurrent) { "Attempted to get current item from iterator before next() was called" }
    return current
  }

  protected fun hasCurrent(): Boolean {
    return hasCurrent
  }

  override fun hasNext(): Boolean {
    return baseIterator.hasNext()
  }

  override fun next(): E {
    current = baseIterator.next()
    hasCurrent = true
    return current!!
  }

  override fun remove() {
    baseIterator.remove()
    onRemove()
  }

  private fun onRemove() {
    current = null
    hasCurrent = false
  }
}