package omnia.data.cache;

import omnia.contract.Container;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class WeakCache<K, V> implements Container<K> {
  private final Map<K, WeakReference<V>> cache = new HashMap<>();

  public WeakCache() {}

  @Override
  public boolean contains(K key) {
    WeakReference<V> ref = cache.get(key);
    return ref != null && ref.get() != null;
  }

  public V getOrCache(K key, Supplier<V> factory) {
    WeakReference<V> cachedReference = cache.get(key);
    V cachedResult = cachedReference != null ? cachedReference.get() : null;
    if (cachedResult == null) {
      cachedResult = factory.get();
      cache.put(key, new WeakReference<>(cachedResult));
    }
    return cachedResult;
  }
}
