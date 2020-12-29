package omnia.data.cache

import java.util.function.DoubleSupplier
import java.util.function.IntSupplier
import java.util.function.LongSupplier
import java.util.function.Supplier
import omnia.contract.Holder

/**
 * A memoized object is one whose value is calculated once and retained indefinitely in memory.
 * Unlike a cached value, a memoized value will never change, cannot be invalidated, and should be
 * based on constant parameters.
 *
 * @param T the type empty object to be memoized
 */
interface Memoized<T : Any> : Holder<T> {

  /**
   * Gets the value represented by this object, optionally computed if the value has not already
   * been memoized. This value must never be null.
   *
   * @return the non-null memoized value
   */
  override fun value(): T

  companion object {

    /**
     * Creates a [Memoized] implementation that returns the provided value verbatim.
     *
     * @param value the value for the Memoized to hold
     * @param T the type of object to be memoized
     * @return a new [Memoized] instance that simply holes the provided value
     */
    @JvmStatic
    fun <T : Any> just(value: T): Memoized<T> {
      return SimpleMemoizer(value)
    }

    /**
     * Creates a [Memoized] implementation that uses the provided [Supplier] as the
     * factory that supplies the value to be memoized.
     *
     * @param supplier the supplier that will create the value to be memoized
     * @param T the type of object to be memoized
     * @return a new [Memoized] instance that will memoize the created value
     */
    @JvmStatic
    fun <T : Any> memoize(supplier: Supplier<out T>): Memoized<T> {
      return SimpleMemoizer(supplier)
    }

    /**
     * Creates a [MemoizedInt] implementation that uses the provided [IntSupplier] as the
     * factory that supplies the value to be memoized.
     *
     * @param supplier the supplier that will create the value to be memoized
     * @return a new [MemoizedInt] instance that will memoize the created value
     */
    @JvmStatic
    fun memoizeInt(supplier: IntSupplier): MemoizedInt {
      return SimpleIntMemoizer(supplier)
    }

    /**
     * Creates a [MemoizedLong] implementation that uses the provided [LongSupplier]
     * as the factory that supplies the value to be memoized.
     *
     * @param supplier the supplier that will create the value to be memoized
     * @return a new [MemoizedLong] instance that will memoize the created value
     */
    @JvmStatic
    fun memoizeLong(supplier: LongSupplier): MemoizedLong {
      return SimpleLongMemoizer(supplier)
    }

    /**
     * Creates a [MemoizedDouble] implementation that uses the provided [DoubleSupplier]
     * as the factory that supplies the value to be memoized.
     *
     * @param supplier the supplier that will create the value to be memoized
     * @return a new [MemoizedDouble] instance that will memoize the created value
     */
    @JvmStatic
    fun memoizeDouble(supplier: DoubleSupplier): MemoizedDouble {
      return SimpleDoubleMemoizer(supplier)
    }
  }
}