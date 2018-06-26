package omnia.data.cache;

import omnia.contract.DoubleHolder;

import java.util.function.DoubleSupplier;

/**
 * A cached double is one whose value is computed once and retrained indefinitely in memory until
 * rendered invalid by calling {@link #invalidate()}.
 *
 * @see Cached for a more general-purpose cache
 */
public interface CachedDouble extends CachedValue, DoubleHolder {

  /**
   * Get the cached value of this double. If no value is cached or the cache is invalid, will
   * cause the value to be recomputed and then cached for subsequent calls.
   *
   * @return the cached double value
   */
  @Override double value();

  /**
   * Creates a {@link CachedDouble} implementation that uses the provided {@link DoubleSupplier} as
   * the factory that supplies the value to be cached.
   *
   * @param supplier the supplier that will construct the given value
   * @return a new {@link CachedDouble} instance that will cache the created value
   */
  static CachedDouble cache(DoubleSupplier supplier) {
    return new SimpleDoubleCacher<>(supplier);
  }
}
