package omnia.data.cache

import java.util.function.IntSupplier

/**
 * A [CachedInt] implementation that uses the given [IntSupplier] to provide the value
 * to be cached. This class is not thread-safe.
 *
 * The given [IntSupplier.getAsInt] method is never invoked if the value is cached.
 */
internal class SimpleIntCacher(private val supplier: IntSupplier) : CachedInt {
  private var isValid = false
  private var value = 0
  override fun value(): Int {
    if (!isValid) {
      value = supplier.asInt
      isValid = true
    }
    return value
  }

  override fun invalidate() {
    isValid = false
  }

}