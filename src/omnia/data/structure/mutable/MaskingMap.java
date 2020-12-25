package omnia.data.structure.mutable;

import static java.util.Objects.requireNonNull;
import static omnia.data.stream.Collectors.toImmutableSet;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import omnia.data.iterate.MappingIterator;
import omnia.data.structure.Collection;
import omnia.data.structure.Set;

class MaskingMap<K, V> implements MutableMap<K, V> {

  private final java.util.Map<K, V> javaMap;

  MaskingMap(java.util.Map<K, V> javaMap) {
    this.javaMap = javaMap;
  }

  @Override
  public void putMapping(K key, V value) {
    javaMap.put(key, value);
  }

  @Override
  public V putMappingIfAbsent(K key, Supplier<V> value) {
    return javaMap.computeIfAbsent(key, unused -> value.get());
  }

  @Override
  public Optional<V> removeUnknownTypedKey(Object key) {
    return Optional.ofNullable(javaMap.remove(key));
  }

  @Override
  public Set<K> keys() {
    return Set.masking(javaMap.keySet());
  }

  @Override
  public Collection<V> values() {
    return Collection.masking(javaMap.values());
  }

  @Override
  public Set<Entry<K, V>> entries() {
    return new Set<>() {

      @Override
      public boolean containsUnknownTyped(Object element) {
        return element instanceof Entry<?, ?>
            && Objects.equals(
                javaMap.get(((Entry<?, ?>) element).key()), ((Entry<?, ?>) element).value());
      }

      @Override
      public Stream<Entry<K, V>> stream() {
        return javaMap.entrySet().stream().map(Entry::masking);
      }

      @Override
      public int count() {
        return javaMap.size();
      }

      @Override
      public Iterator<Entry<K, V>> iterator() {
        return new MappingIterator<>(javaMap.entrySet().iterator(), Entry::masking);
      }
    };
  }

  @Override
  public Optional<V> valueOfUnknownTyped(Object key) {
    return Optional.ofNullable(javaMap.get(key));
  }

  @Override
  public Set<K> keysOfUnknownTyped(Object value) {
    return javaMap.entrySet()
        .stream()
        .filter(e -> Objects.equals(e.getValue(), value))
        .map(java.util.Map.Entry::getKey)
        .collect(toImmutableSet());
  }

  @Override
  public String toString() {
    return javaMap.toString();
  }
}
