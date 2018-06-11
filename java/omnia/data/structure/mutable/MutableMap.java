package omnia.data.structure.mutable;

import omnia.data.structure.Map;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public interface MutableMap<K, V> extends Map<K, V> {

  void putMapping(K key, V value);

  Optional<V> removeKey(K key);

  @Override MutableSet<Entry<K, V>> entries();

  static <K, V> MutableMap<K, V> masking(java.util.Map<K, V> javaMap) {
    return new MaskingMap<>(javaMap);
  }
}
