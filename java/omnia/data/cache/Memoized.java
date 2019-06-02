package omnia.data.cache;

import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
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

  /**
   * Creates a {@link MemoizedInt} implementation that uses the provided {@link IntSupplier} as the
   * factory that supplies the value to be memoized.
   *
   * @param supplier the supplier that will create the value to be memoized
   * @return a new {@link MemoizedInt} instance that will memoize the created value
   */
  static MemoizedInt memoizeInt(IntSupplier supplier) {
    return new SimpleIntMemoizer(supplier);
  }

  /**
   * Creates a {@link MemoizedLong} implementation that uses the provided {@link LongSupplier}
   * as the factory that supplies the value to be memoized.
   *
   * @param supplier the supplier that will create the value to be memoized
   * @return a new {@link MemoizedLong} instance that will memoize the created value
   */
  static MemoizedLong memoizeLong(LongSupplier supplier) {
    return new SimpleLongMemoizer(supplier);
  }

  /**
   * Creates a {@link MemoizedDouble} implementation that uses the provided {@link DoubleSupplier}
   * as the factory that supplies the value to be memoized.
   *
   * @param supplier the supplier that will create the value to be memoized
   * @return a new {@link MemoizedDouble} instance that will memoize the created value
   */
  static MemoizedDouble memoizeDouble(DoubleSupplier supplier) {
    return new SimpleDoubleMemoizer(supplier);
  }
}
