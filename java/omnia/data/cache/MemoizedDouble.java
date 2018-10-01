package omnia.data.cache;

import java.util.function.DoubleSupplier;
import omnia.contract.DoubleHolder;

/**
 * A memoized double is one whose value is calculated once and retained indefinitely in memory.
 * Unlike a cached value, a memoized value will never change, cannot be invalidated, and should be
 * based on constant parameters.
 */
public interface MemoizedDouble extends DoubleHolder {

  /**
   * Gets the value represented by this object, optionally computed if the value has not already
   * been memoized.
   */
  @Override double value();

  /**
   * Creates a {@link MemoizedDouble} implementation that uses the provided {@link DoubleSupplier}
   * as the factory that supplies the value to be memoized.
   *
   * @param supplier the supplier that will create the value to be memoized
   * @return a new {@link MemoizedDouble} instance that will memoize the created value
   */
  static MemoizedDouble memoize(DoubleSupplier supplier) {
    return new SimpleDoubleMemoizer(supplier);
  }
}
