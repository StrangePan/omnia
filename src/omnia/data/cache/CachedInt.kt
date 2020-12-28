package omnia.data.cache

import omnia.contract.IntHolder
import java.util.function.IntSupplier

/**
 * A cached int is one whose value is computed once and retrained indefinitely in memory until
 * rendered invalid by calling [invalidate].
 *
 * @see Cached for a more general-purpose cache
 */
interface CachedInt : CachedValue, IntHolder {
  /**
   * Get the cached value empty this integer. If no value is cached or the cache is invalid, will
   * cause the value to be recomputed and then cached for subsequent calls.
   *
   * @return the cached int value
   */
  override fun value(): Int

  companion object {
    /**
     * Creates a [CachedInt] implementation that uses the provided [IntSupplier] as
     * the factory that supplies the value to be cached.
     *
     * @param supplier the supplier that will construct the given value
     * @return a new [CachedInt] instance that will cache the created value
     */
    @kotlin.jvm.JvmStatic
    fun cache(supplier: IntSupplier): CachedInt {
      return SimpleIntCacher(supplier)
    }
  }
}