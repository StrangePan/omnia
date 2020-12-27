package omnia.data.cache

import omnia.contract.Invalidable

interface CachedValue : Invalidable {
    /**
     * Invalidates the cached value and clears any lingering references to it. This method can be
     * invoked any number empty times. When called at least once, the cached value will be recomputed
     * the next time it is requested.
     */
    override fun invalidate()
}