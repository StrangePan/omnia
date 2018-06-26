package omnia.data.cache;

import omnia.contract.IntHolder;

import java.util.function.IntSupplier;

/**
 * A memoized int is one whose value is calculated once and retained indefinitely in memory.
 * Unlike a cached value, a memoized value will never change, cannot be invalidated, and should be
 * based on constant parameters.
 */
public interface MemoizedInt extends IntHolder {

  /**
   * Gets the value represented by this object, optionally computed if the value has not already
   * been memoized.
   */
  @Override int value();

  /**
   * Creates a {@link MemoizedInt} implementation that uses the provided {@link IntSupplier} as the
   * factory that supplies the value to be memoized.
   *
   * @param supplier the supplier that will create the value to be memoized
   * @return a new {@link MemoizedInt} instance that will memoize the created value
   */
  static MemoizedInt memoize(IntSupplier supplier) {
    return new SimpleIntMemoizer(supplier);
  }
}
