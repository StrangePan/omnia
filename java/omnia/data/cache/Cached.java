package omnia.data.cache;

import java.util.function.Supplier;
import omnia.contract.Holder;
import omnia.contract.Invalidable;

/**
 * A cached value is one that is lazily computed and temporary cached until it is invalidated.
 *
 * Cached values can be invalidated by calling {@link #invalidate()}. The next time {@link
 * #value()} is called, the value will be recomputed.
 *
 * @param <T> the type of object to be cached
 */
public interface Cached<T> extends Holder<T>, Invalidable {

  /**
   * Get the cached value reference. If no value is cached or the cache is invalid, will cause the
   * value to be recomputed and then cached for subsequent calls.
   *
   * @return the cached object reference
   */
  @Override T value();

  /**
   * Invalidates the cached value and clears any lingering references to it. This method can be
   * invoked any number of times. The next time {@link #value()} is called after invoking this
   * method will cause the value to be recomputed.
   */
  @Override void invalidate();

  /**
   * Creates a {@link Cached} implementation that uses the provided {@link Supplier} as the
   * factory that supplies the value to be cached.
   *
   * @param supplier the supplier that will construct the given value
   * @param <T> the type of object that will be cached
   * @return a new {@link Cached} instance that will cache the created value
   */
  static <T> Cached<T> cache(Supplier<T> supplier) {
    return new SimpleCacher<>(supplier);
  }
}
