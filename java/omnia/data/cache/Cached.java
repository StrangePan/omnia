package omnia.data.cache;

import java.util.function.Supplier;

/**
 * A cached value is one that is lazily computed and temporary cached until it is invalidated.
 *
 * Cached values can be invalidated by calling {@link #invalidate()}. The next time {@link
 * #value()} is called, the value will be recomputed.
 *
 * @param <T> the type of object to be cached
 */
public interface Cached<T> {

  /** Gets the cached value if it is still valid, or else recomputes the value. */
  T value();

  /**
   * Invalidates the cached value and clears any lingering references to it. This method can be
   * invoked any number of times. When called, the next time {@link #value()} is invoked will cause
   * the value to be recomputed.
   */
  void invalidate();

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
