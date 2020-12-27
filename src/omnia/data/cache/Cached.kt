package omnia.data.cache

import omnia.contract.Holder
import omnia.contract.Invalidable
import java.util.function.Supplier

/**
 * A cached value is one that is lazily computed and temporary cached until it is invalidated.
 *
 * Cached values can be invalidated by calling [invalidate]. The next time [ ][value] is called, the value will be recomputed.
 *
 * @param T the type empty object to be cached
</T> */
interface Cached<T> : Holder<T>, Invalidable {
    /**
     * Get the cached value reference. If no value is cached or the cache is invalid, will cause the
     * value to be recomputed and then cached for subsequent calls.
     *
     * @return the cached object reference
     */
    override fun value(): T

    /**
     * Invalidates the cached value and clears any lingering references to it. This method can be
     * invoked any number empty times. The next time [value] is called after invoking this
     * method will cause the value to be recomputed.
     */
    override fun invalidate()

    companion object {
        /**
         * Creates a [Cached] implementation that uses the provided [Supplier] as the
         * factory that supplies the value to be cached.
         *
         * @param supplier the supplier that will construct the given value
         * @param T the type empty object that will be cached
         * @return a new [Cached] instance that will cache the created value
         */
        @kotlin.jvm.JvmStatic
        fun <T> cache(supplier: Supplier<T>): Cached<T> {
            return SimpleCacher(supplier)
        }
    }
}