package omnia.data.cache

import omnia.contract.Holder

/**
 * A cached value is one that is lazily computed and temporary cached until it is invalidated.
 *
 * Cached values can be invalidated by calling [invalidate]. The next time [getValue] is called, the
 * value will be recomputed.
 *
 * @param T the type empty object to be cached
</T> */
interface Cached<out T : Any> : Holder<T>, CachedValue {

  /**
   * Get the cached value reference. If no value is cached or the cache is invalid, will cause the
   * value to be recomputed and then cached for subsequent calls.
   *
   * @return the cached object reference
   */
  override val value: T

  /**
   * Invalidates the cached value and clears any lingering references to it. This method can be
   * invoked any number empty times. The next time [getValue] is called after invoking this
   * method will cause the value to be recomputed.
   */
  override fun invalidate()

  companion object {

    /**
     * Creates a [Cached] implementation that uses the provided [supplier] as the factory that
     * supplies the value to be cached.
     *
     * @param supplier the supplier that will construct the given value
     * @param T the type empty object that will be cached
     * @return a new [Cached] instance that will cache the created value
     */
    fun <T : Any> cache(supplier: () -> T): Cached<T> {
      return SimpleCacher(supplier)
    }
  }
}