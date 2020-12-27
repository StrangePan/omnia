package omnia.data.cache

import omnia.contract.DoubleHolder
import java.util.function.DoubleSupplier

/**
 * A memoized double is one whose value is calculated once and retained indefinitely in memory.
 * Unlike a cached value, a memoized value will never change, cannot be invalidated, and should be
 * based on constant parameters.
 */
interface MemoizedDouble : DoubleHolder {
    /**
     * Gets the value represented by this object, optionally computed if the value has not already
     * been memoized.
     */
    override fun value(): Double

    companion object {
        /**
         * Creates a [MemoizedDouble] implementation that uses the provided [DoubleSupplier]
         * as the factory that supplies the value to be memoized.
         *
         * @param supplier the supplier that will create the value to be memoized
         * @return a new [MemoizedDouble] instance that will memoize the created value
         */
        @kotlin.jvm.JvmStatic
        fun memoize(supplier: DoubleSupplier): MemoizedDouble? {
            return SimpleDoubleMemoizer(supplier)
        }
    }
}