package omnia.data.structure.mutable;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import omnia.data.iterate.MappingIterator;
import omnia.data.iterate.WrapperIterator;
import omnia.data.structure.BidirectionalMap;
import omnia.data.structure.Set;

public class HashBidirectionalMap<K, V> implements MutableBidirectionalMap<K, V> {
  private final HashMap<K, Entry<K, V>> keyMap = HashMap.create();
  private final HashMap<V, Entry<K, V>> valueMap = HashMap.create();
  private final InverseMap inverseMap = new InverseMap();

  @Override
  public MutableBidirectionalMap<V, K> inverse() {
    return inverseMap;
  }

  @Override
  public Set<K> keys() {
    return keyMap.keys();
  }

  @Override
  public Set<V> values() {
    return valueMap.keys();
  }

  @Override
  public void putMapping(K key, V value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public V putMappingIfAbsent(K key, Supplier<V> value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Optional<V> removeKey(K key) {
    Optional<Entry<K, V>> entry = keyMap.removeKey(key);
    entry.ifPresent(kvEntry -> valueMap.removeKey(kvEntry.value()));
    return entry.map(Entry::value);
  }

  @Override
  public MutableSet<Entry<K, V>> entries() {
    return new BidirectionalMapSet();
  }

  @Override
  public Optional<V> valueOf(Object key) {
    return keyMap.valueOf(key).map(Entry::value);
  }

  @Override
  public Set<K> keysOf(Object value) {
    return Set.masking(
        valueMap.valueOf(value)
            .map(Entry::key)
            .map(Collections::singleton)
            .orElse(Collections.emptySet()));
  }

  private class BidirectionalMapSet implements MutableSet<Entry<K, V>> {
    @Override
    public void add(Entry<K, V> element) {
      Entry<K, V> entry = Entry.of(element.key(), element.value());
      if (contains(entry)) {
        return;
      }
      keyMap.removeKey(entry.key());
      valueMap.removeKey(entry.value());
      keyMap.putMapping(entry.key(), entry);
      valueMap.putMapping(entry.value(), entry);
    }

    @Override
    public boolean remove(Entry<K, V> element) {
      Entry<K, V> entry = Entry.of(element.key(), element.value());
      if (!contains(entry)) {
        return false;
      }
      keyMap.removeKey(entry.key());
      valueMap.removeKey(entry.value());
      return true;
    }

    @Override
    public void clear() {
      keyMap.entries().clear();
      valueMap.entries().clear();
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
      return new WrapperIterator<>(
          new MappingIterator<>(keyMap.entries().iterator(), Entry::value)) {

        @Override
        public void remove() {
          valueMap.removeKey(current().value());
          super.remove();
        }
      };
    }

    @Override
    public boolean contains(Object element) {
      if (!(element instanceof Entry)) {
        return false;
      }
      Entry<?, ?> entry = Entry.of(((Entry) element).key(), ((Entry) element).value());
      return keyMap.valueOf(entry.key()).map(entry::equals).orElse(false);
    }

    @Override
    public int count() {
      return keyMap.entries().count();
    }

    @Override
    public boolean isPopulated() {
      return keyMap.entries().isPopulated();
    }

    @Override
    public Stream<Entry<K, V>> stream() {
      return keyMap.entries().stream().map(Entry::value);
    }
  }

  private class InverseMap implements MutableBidirectionalMap<V, K> {

    @Override
    public MutableBidirectionalMap<K, V> inverse() {
      return HashBidirectionalMap.this;
    }

    @Override
    public Set<V> keys() {
      return HashBidirectionalMap.this.values();
    }

    @Override
    public Set<K> values() {
      return HashBidirectionalMap.this.keys();
    }

    @Override
    public void putMapping(V key, K value) {
      HashBidirectionalMap.this.putMapping(value, key);
    }

    @Override
    public K putMappingIfAbsent(V key, Supplier<K> value) {
      throw new UnsupportedOperationException();
    }

    @Override
    public Optional<K> removeKey(V key) {
      Optional<K> realKey = HashBidirectionalMap.this.valueMap.valueOf(key).map(Entry::key);
      realKey.ifPresent(HashBidirectionalMap.this::removeKey);
      return realKey;
    }

    @Override
    public MutableSet<Entry<V, K>> entries() {
      MutableSet<Entry<K, V>> baseSet = HashBidirectionalMap.this.entries();
      return new MutableSet<>() {

        @Override
        public Stream<Entry<V, K>> stream() {
          return baseSet.stream().map(HashBidirectionalMap::invert);
        }

        @Override
        public int count() {
          return baseSet.count();
        }

        @Override
        public boolean isPopulated() {
          return baseSet.isPopulated();
        }

        @Override
        public boolean contains(Object element) {
          return element instanceof Entry && baseSet.contains(invert((Entry<?,?>) element));
        }

        @Override
        public Iterator<Entry<V, K>> iterator() {
          return new MappingIterator<>(baseSet.iterator(), HashBidirectionalMap::invert);
        }

        @Override
        public void add(Entry<V, K> element) {
          baseSet.add(invert(element));
        }

        @Override
        public boolean remove(Entry<V, K> element) {
          return baseSet.remove(invert(element));
        }

        @Override
        public void clear() {
          baseSet.clear();
        }
      };
    }

    @Override
    public Optional<K> valueOf(Object key) {
      return HashBidirectionalMap.this.valueMap.valueOf(key).map(Entry::key);
    }

    @Override
    public Set<V> keysOf(Object value) {
      return Set.masking(
          HashBidirectionalMap.this
              .valueOf(value)
              .map(Collections::singleton)
              .orElse(Collections.emptySet()));
    }
  }

  private static <K, V> Entry<V, K> invert(Entry<K, V> e) {
    return Entry.of(e.value(), e.key());
  }

  public static <K, V> HashBidirectionalMap<K, V> copyOf(BidirectionalMap<K, V> original) {
    HashBidirectionalMap<K, V> copy = new HashBidirectionalMap<>();
    original.entries().forEach(copy.entries()::add);
    return copy;
  }
}
