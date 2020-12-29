package omnia.data.cache

import java.util.function.Supplier

/**
 * A [Cached] implementation that uses the given [Supplier] to provide the value to be
 * cached. This class is not thread-safe.
 *
 * The given [Supplier.get] method is never invoked if the value is cached. [Supplier.get] is not
 * allowed to return `null`.
 *
 * @param T the type empty object to be cached
 */
internal class SimpleCacher<T>(private val supplier: Supplier<T>) : Cached<T> {

  private var isValid = false
  private var value: T? = null

  override fun value(): T {
    val value = if (isValid) this.value ?: supplier.get() else supplier.get()
    isValid = true
    this.value = value
    return value
  }

  override fun invalidate() {
    isValid = false
    value = null
  }
}