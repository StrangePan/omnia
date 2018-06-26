package omnia.data.cache;

import omnia.contract.IntHolder;

import java.util.function.IntSupplier;

/**
 * A cached int is one whose value is computed once and retrained indefinitely in memory until
 * rendered invalid by calling {@link #invalidate()}.
 *
 * @see Cached for a more general-purpose cache
 */
public interface CachedInt extends CachedValue, IntHolder {

  /**
   * Get the cached value of this integer. If no value is cached or the cache is invalid, will
   * cause the value to be recomputed and then cached for subsequent calls.
   *
   * @return the cached int value
   */
  @Override int value();

  /**
   * Creates a {@link CachedInt} implementation that uses the provided {@link IntSupplier} as
   * the factory that supplies the value to be cached.
   *
   * @param supplier the supplier that will construct the given value
   * @return a new {@link CachedInt} instance that will cache the created value
   */
  static CachedInt cache(IntSupplier supplier) {
    return new SimpleIntCacher<>(supplier);
  }
}
