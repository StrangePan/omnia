package omnia.data.cache


/**
 * A [MemoizedLong] implementation that uses a client-given [LongSupplier] to
 * lazily compute the memoized value. This class is thread-safe.
 *
 * Once computed and memoized, the [LongSupplier] reference is forgotten and never invoked
 * ever again. The given [LongSupplier.getAsLong] method is never invoked more than
 * once.
 */
internal class SimpleLongMemoizer(supplier: () -> Long) : MemoizedLong {

  @Volatile
  private var supplier: (() -> Long)? = supplier

  @Volatile
  private var value: Long = 0
  override fun value(): Long {
    if (supplier != null) {
      synchronized(this) {
        if (supplier != null) {
          value = supplier!!()
          supplier = null
        }
      }
    }
    return value
  }
}