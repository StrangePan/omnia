package omnia.data.cache;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import omnia.contract.Container;

/**
 * A cache implementation that stores items using {@link WeakReferences} as a form of lazy,
 * optional caching.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class WeakCache<K, V> implements Container {
  private final Map<K, WeakReference<V>> cache = new HashMap<>();

  /** Creates a new, empty cache. */
  public WeakCache() {}

  /** Checks if the cache contains the given key.
   * @param key*/
  @Override
  public boolean contains(Object key) {
    WeakReference<V> ref = cache.get(key);
    return ref != null && ref.get() != null;
  }

  /**
   * Retrieves and returns the cached value associated with {@code key} or, if necessary, resolves
   * the given {@link Supplier} and stores its result in the cache.
   *
   * @param key the key to retrieve and, if unavailable, store the result of {@code factory} at
   * @param factory the factory function to invoke if the desired cached value is missing
   * @return the cached value if it exists or else the result of invoking {@link Supplier#get()} on
   *     {@code factory}
   */
  public V getOrCache(K key, Supplier<V> factory) {
    WeakReference<V> cachedReference = cache.get(key);
    V cachedResult = cachedReference != null ? cachedReference.get() : null;
    if (cachedResult == null) {
      cachedResult = factory.get();
      cache.put(key, new WeakReference<>(cachedResult));
    }
    return cachedResult;
  }

  /** Retrieves the cached value associated with {@code key} if it exists. */
  public Optional<V> get(K key) {
    WeakReference<V> cachedReference = cache.get(key);
    V cachedResult = cachedReference != null ? cachedReference.get() : null;
    return Optional.ofNullable(cachedResult);
  }
}
