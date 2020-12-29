package omnia.data.cache

import java.util.function.LongSupplier

/**
 * A [CachedLong] implementation that uses the given [LongSupplier] to provide the
 * value to be cached. This class is not thread-safe.
 *
 * The given [LongSupplier.getAsLong] method is never invoked if the value is cached.
 */
internal class SimpleLongCacher(private val supplier: LongSupplier) : CachedLong {

  private var isValid = false
  private var value: Long = 0
  override fun value(): Long {
    if (!isValid) {
      value = supplier.asLong
      isValid = true
    }
    return value
  }

  override fun invalidate() {
    isValid = false
  }
}