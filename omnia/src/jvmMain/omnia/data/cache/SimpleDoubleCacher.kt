package omnia.data.cache


/**
 * A [CachedDouble] implementation that uses the given [DoubleSupplier] to provide the
 * value to be cached. This class is not thread-safe.
 *
 * The given [DoubleSupplier.getAsDouble] method is never invoked if the value is cached.
 */
internal class SimpleDoubleCacher(private val supplier: () -> Double) : CachedDouble {

  private var isValid = false
  private var value = 0.0
  override fun value(): Double {
    val value = if (isValid) this.value else supplier()
    isValid = true
    this.value = value
    return value
  }

  override fun invalidate() {
    isValid = false
  }
}