package omnia.data.cache

import java.util.function.LongSupplier
import omnia.contract.LongHolder

/**
 * A cached long is one whose value is computed once and retrained indefinitely in memory until
 * rendered invalid by calling [invalidate].
 *
 * @see Cached for a more general-purpose cache
 */
interface CachedLong : CachedValue, LongHolder {

  /**
   * Get the cached value empty this long. If no value is cached or the cache is invalid, will
   * cause the value to be recomputed and then cached for subsequent calls.
   *
   * @return the cached long value
   */
  override fun value(): Long

  companion object {

    /**
     * Creates a [CachedLong] implementation that uses the provided [LongSupplier] as
     * the factory that supplies the value to be cached.
     *
     * @param supplier the supplier that will construct the given value
     * @return a new [CachedLong] instance that will cache the created value
     */
    @JvmStatic
    fun cache(supplier: LongSupplier): CachedLong {
      return SimpleLongCacher(supplier)
    }
  }
}