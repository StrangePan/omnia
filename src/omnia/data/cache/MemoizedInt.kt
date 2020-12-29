package omnia.data.cache

import java.util.function.IntSupplier
import omnia.contract.IntHolder

/**
 * A memoized int is one whose value is calculated once and retained indefinitely in memory.
 * Unlike a cached value, a memoized value will never change, cannot be invalidated, and should be
 * based on constant parameters.
 */
interface MemoizedInt : IntHolder {

  /**
   * Gets the value represented by this object, optionally computed if the value has not already
   * been memoized.
   */
  override fun value(): Int

  companion object {

    /**
     * Creates a [MemoizedInt] implementation that uses the provided [IntSupplier] as the
     * factory that supplies the value to be memoized.
     *
     * @param supplier the supplier that will create the value to be memoized
     * @return a new [MemoizedInt] instance that will memoize the created value
     */
    @JvmStatic
    fun memoize(supplier: IntSupplier): MemoizedInt {
      return SimpleIntMemoizer(supplier)
    }
  }
}