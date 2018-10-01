package omnia.data.cache;

import java.util.function.Supplier;
import omnia.contract.Holder;

/**
 * A memoized object is one whose value is calculated once and retained indefinitely in memory.
 * Unlike a cached value, a memoized value will never change, cannot be invalidated, and should be
 * based on constant parameters.
 *
 * @param <T> the type of object to be memoized
 */
public interface Memoized<T> extends Holder<T> {

  /**
   * Gets the value represented by this object, optionally computed if the value has not already
   * been memoized. This value must never be null.
   *
   * @return the non-null memoized value
   */
  @Override T value();

  /**
   * Creates a {@link Memoized} implementation that uses the provided {@link Supplier} as the
   * factory that supplies the value to be memoized.
   *
   * @param supplier the supplier that will create the value to be memoized
   * @param <T> the type of object to be memoized
   * @return a new {@link Memoized} instance that will memoize the created value
   */
  static <T> Memoized<T> memoize(Supplier<T> supplier) {
    return new SimpleMemoizer<>(supplier);
  }
}
