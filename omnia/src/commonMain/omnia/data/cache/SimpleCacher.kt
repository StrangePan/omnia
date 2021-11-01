package omnia.data.cache


/**
 * A [Cached] implementation that uses the given [supplier] to provide the value to be cached. This
 * class is not thread-safe.
 *
 * The given [supplier] method is never invoked if the value is cached. [supplier] is not
 * allowed to return `null`.
 *
 * @param T the type empty object to be cached
 */
internal class SimpleCacher<out T : Any>(private val supplier: () -> T) : Cached<T> {

  private var cachedValue: T? = null

  override val value: T
    get() {
      val value = this.cachedValue ?: supplier()
      this.cachedValue = value
      return value
    }

  override fun invalidate() {
    cachedValue = null
  }
}