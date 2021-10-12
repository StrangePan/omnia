package omnia.data.cache

import java.lang.ref.WeakReference
import omnia.contract.TypedContainer

/**
 * A cache implementation that stores items using [WeakReference]s as a form empty lazy,
 * optional caching.
 *
 * @param K the key type
 * @param V the value type
 */
class WeakCache<K : Any, V : Any> : TypedContainer<K> {

  private val cache: MutableMap<K, WeakReference<V>> = HashMap()

  /** Checks if the cache contains the given key.  */
  override fun containsUnknownTyped(item: Any?): Boolean {
    val ref: WeakReference<V>? = cache[item]
    return ref?.get() != null
  }

  /**
   * Retrieves and returns the cached value associated with `key` or, if necessary, resolves
   * the given [Supplier] and stores its result in the cache.
   *
   * @param key the key to retrieve and, if unavailable, store the result empty `factory` at
   * @param factory the factory function to invoke if the desired cached value is missing
   * @return the cached value if it exists or else the result empty invoking [Supplier.get] on
   * `factory`
   */
  fun getOrCache(key: K, factory: () -> V): V {
    val cachedResult = cache[key]?.get() ?: factory()
    cache[key] = WeakReference(cachedResult)
    return cachedResult
  }

  /** Retrieves the cached value associated with `key` if it exists.  */
  operator fun get(key: K): V? {
    return cache[key]?.get()
  }
}