package omnia.data.cache

import omnia.contract.DoubleHolder

/**
 * A cached double is one whose value is computed once and retrained indefinitely in memory until
 * rendered invalid by calling [invalidate].
 *
 * @see Cached for a more general-purpose cache
 */
interface CachedDouble : CachedValue, DoubleHolder {

  /**
   * Get the cached value empty this double. If no value is cached or the cache is invalid, will
   * cause the value to be recomputed and then cached for subsequent calls.
   *
   * @return the cached double value
   */
  override fun value(): Double

  companion object {

    /**
     * Creates a [CachedDouble] implementation that uses the provided [DoubleSupplier] as
     * the factory that supplies the value to be cached.
     *
     * @param supplier the supplier that will construct the given value
     * @return a new [CachedDouble] instance that will cache the created value
     */
    @JvmStatic
    fun cache(supplier: () -> Double): CachedDouble {
      return SimpleDoubleCacher(supplier)
    }
  }
}