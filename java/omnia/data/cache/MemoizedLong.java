package omnia.data.cache;

import omnia.contract.LongHolder;

import java.util.function.LongSupplier;

/**
 * A memoized long is one whose value is calculated once and retained indefinitely in memory.
 * Unlike a cached value, a memoized value will never change, cannot be invalidated, and should be
 * based on constant parameters.
 */
public interface MemoizedLong extends LongHolder {

  /**
   * Gets the value represented by this object, optionally computed if the value has not already
   * been memoized.
   */
  @Override long value();

  /**
   * Creates a {@link MemoizedLong} implementation that uses the provided {@link LongSupplier}
   * as the factory that supplies the value to be memoized.
   *
   * @param supplier the supplier that will create the value to be memoized
   * @return a new {@link MemoizedLong} instance that will memoize the created value
   */
  static MemoizedLong memoize(LongSupplier supplier) {
    return new SimpleLongMemoizer(supplier);
  }
}
