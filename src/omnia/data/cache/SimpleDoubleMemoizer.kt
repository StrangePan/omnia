package omnia.data.cache

import java.util.function.DoubleSupplier

/**
 * A [MemoizedDouble] implementation that uses a client-given [DoubleSupplier] to
 * lazily compute the memoized value. This class is thread-safe.
 *
 * Once computed and memoized, the [DoubleSupplier] reference is forgotten and never invoked
 * ever again. The given [DoubleSupplier.getAsDouble] method is never invoked more than
 * once.
 */
internal class SimpleDoubleMemoizer(supplier: DoubleSupplier) : MemoizedDouble {
  @Volatile
  private var supplier: DoubleSupplier? = supplier

  @Volatile
  private var value = 0.0
  override fun value(): Double {
    if (supplier != null) {
      synchronized(this) {
        if (supplier != null) {
          value = supplier!!.asDouble
          supplier = null
        }
      }
    }
    return value
  }
}