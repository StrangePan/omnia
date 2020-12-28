package omnia.data.cache

import java.util.function.IntSupplier

/**
 * A [MemoizedInt] implementation that uses a client-given [IntSupplier] to lazily
 * compute the memoized value. This class is thread-safe.
 *
 * Once computed and memoized, the [IntSupplier] reference is forgotten and never invoked ever
 * again. The given [IntSupplier.getAsInt] method is never invoked more than once.
 */
internal class SimpleIntMemoizer(supplier: IntSupplier) : MemoizedInt {
  @Volatile
  private var supplier: IntSupplier? = supplier

  @Volatile
  private var value = 0
  override fun value(): Int {
    if (supplier != null) {
      synchronized(this) {
        if (supplier != null) {
          value = supplier!!.asInt
          supplier = null
        }
      }
    }
    return value
  }
}