package omnia.data.structure.mutable;

import java.util.Optional;
import java.util.function.Supplier;
import omnia.data.structure.Map;

public interface MutableMap<K, V> extends Map<K, V> {

  void putMapping(K key, V value);

  V putMappingIfAbsent(K key, Supplier<V> value);

  default Optional<V> removeKey(K key) {
    return removeUnknownTypedKey(key);
  }

  Optional<V> removeUnknownTypedKey(Object key);

  @Override MutableSet<Entry<K, V>> entries();

  static <K, V> MutableMap<K, V> masking(java.util.Map<K, V> javaMap) {
    return new MaskingMap<>(javaMap);
  }
}
