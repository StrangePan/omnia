package omnia.data.cache

import java.util.function.DoubleSupplier

/**
 * A [CachedDouble] implementation that uses the given [DoubleSupplier] to provide the
 * value to be cached. This class is not thread-safe.
 *
 * The given [DoubleSupplier.getAsDouble] method is never invoked if the value is cached.
 */
internal class SimpleDoubleCacher(private val supplier: DoubleSupplier) : CachedDouble {

  private var isValid = false
  private var value = 0.0
  override fun value(): Double {
    val value = if (isValid) this.value else supplier.asDouble
    isValid = true
    this.value = value
    return value
  }

  override fun invalidate() {
    isValid = false
  }
}