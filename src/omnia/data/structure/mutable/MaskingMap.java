package omnia.data.structure.mutable;

import static java.util.Objects.requireNonNull;
import static omnia.data.stream.Collectors.toImmutableSet;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
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
  public MutableSet<Entry<K, V>> entries() {
    java.util.Set<java.util.Map.Entry<K, V>> javaEntries = javaMap.entrySet();
    return new MutableSet<>() {

      @Override
      public Stream<Entry<K, V>> stream() {
        return javaEntries.stream().map(Entry::masking);
      }

      @Override
      public boolean isPopulated() {
        return !javaEntries.isEmpty();
      }

      @Override
      public int count() {
        return javaEntries.size();
      }

      @Override
      public boolean containsUnknownTyped(Object element) {
        return javaEntries.stream()
            .map(Entry::masking)
            .anyMatch(e -> Objects.equals(element, e));
      }

      @Override
      public Iterator<Entry<K, V>> iterator() {
        Set<Entry<K, V>> entries =
            javaEntries.stream().map(Entry::masking).collect(toImmutableSet());
        Iterator<Entry<K, V>> entriesIterator = entries.iterator();

        return new Iterator<>() {
          private Entry<K, V> lastEntry = null;

          @Override
          public boolean hasNext() {
            return entriesIterator.hasNext();
          }

          @Override
          public Entry<K, V> next() {
            lastEntry = entriesIterator.next();
            return lastEntry;
          }

          @Override
          public void remove() {
            if (lastEntry == null) {
              throw new IllegalStateException("remove() called on iterator before next()");
            }
            javaMap.remove(lastEntry.key());
          }

          @Override
          public void forEachRemaining(Consumer<? super Entry<K, V>> action) {
            entriesIterator.forEachRemaining(action);
          }
        };
      }

      @Override
      public void add(Entry<K, V> element) {
        requireNonNull(element);
        javaMap.put(element.key(), element.value());
      }

      @Override
      public boolean removeUnknownTyped(Object element) {
        requireNonNull(element);
        return element instanceof Entry<?, ?>
            && javaMap.remove(((Entry<?, ?>) element).key(), ((Entry<?, ?>) element).value());
      }

      @Override
      public void clear() {
        javaMap.clear();
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
}
