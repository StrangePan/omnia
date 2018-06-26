package omnia.data.cache;

import omnia.contract.LongHolder;

import java.util.function.LongSupplier;

/**
 * A cached long is one whose value is computed once and retrained indefinitely in memory until
 * rendered invalid by calling {@link #invalidate()}.
 *
 * @see Cached for a more general-purpose cache
 */
public interface CachedLong extends CachedValue, LongHolder {

  /**
   * Get the cached value of this long. If no value is cached or the cache is invalid, will
   * cause the value to be recomputed and then cached for subsequent calls.
   *
   * @return the cached long value
   */
  @Override long value();

  /**
   * Creates a {@link CachedLong} implementation that uses the provided {@link LongSupplier} as
   * the factory that supplies the value to be cached.
   *
   * @param supplier the supplier that will construct the given value
   * @return a new {@link CachedLong} instance that will cache the created value
   */
  static CachedLong cache(LongSupplier supplier) {
    return new SimpleLongCacher<>(supplier);
  }

}
