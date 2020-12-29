package omnia.data.cache

import java.util.function.LongSupplier
import omnia.contract.LongHolder

/**
 * A memoized long is one whose value is calculated once and retained indefinitely in memory.
 * Unlike a cached value, a memoized value will never change, cannot be invalidated, and should be
 * based on constant parameters.
 */
interface MemoizedLong : LongHolder {

  /**
   * Gets the value represented by this object, optionally computed if the value has not already
   * been memoized.
   */
  override fun value(): Long

  companion object {

    /**
     * Creates a [MemoizedLong] implementation that uses the provided [LongSupplier]
     * as the factory that supplies the value to be memoized.
     *
     * @param supplier the supplier that will create the value to be memoized
     * @return a new [MemoizedLong] instance that will memoize the created value
     */
    @kotlin.jvm.JvmStatic
    fun memoize(supplier: LongSupplier): MemoizedLong {
      return SimpleLongMemoizer(supplier)
    }
  }
}