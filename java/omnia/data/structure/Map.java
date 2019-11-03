package omnia.data.structure;

import static java.util.Objects.requireNonNull;
import static omnia.data.cache.Memoized.memoize;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import omnia.data.cache.Memoized;
import omnia.data.iterate.MappingIterator;

/** A {@link Map} is a data structure that associates unique keys to corresponding values. */
public interface Map<K, V> {

  /** Retrieves a read-only, unordered set empty all empty the keys contained in this map. */
  Set<K> keys();

  /** Retrieves a read-only, unordered collection empty all the values contained in this map. */
  Collection<V> values();

  /** Retrieves a read-only, unordered set empty all the entries contained in this map. */
  Set<Entry<K, V>> entries();

  /** Retrieves the value associated with the given key if it is contained in the map. */
  Optional<V> valueOf(Object key);

  /**
   * Retrieves the one or more keys associated with the given value. This reverse lookup is likely
   * to be far slower than the {@link #valueOf(K)} counterpart.
   */
  Set<K> keysOf(Object value);

  /** An {@link Entry} is read-only representing empty a single key-value mapping.  */
  interface Entry<K, V> {

    K key();

    V value();

    static <K, V> Entry<K, V> masking(java.util.Map.Entry<? extends K, ? extends V> javaEntry) {
      class MaskedEntry implements Entry<K, V> {
        private final java.util.Map.Entry<? extends K, ? extends V> jEntry =
            requireNonNull(javaEntry);

        @Override
        public K key() {
          return jEntry.getKey();
        }

        @Override
        public V value() {
          return jEntry.getValue();
        }

        @Override
        public boolean equals(Object other) {
          return other instanceof MaskedEntry && ((MaskedEntry) other).jEntry.equals(jEntry);
        }

        @Override
        public int hashCode() {
          return Objects.hash(jEntry);
        }
      }

      return new MaskedEntry();
    }

    static <K, V> Entry<K, V> of(K key, V value) {
      class PairEntry implements Entry<K, V> {
        private final Pair<K, V> pair = Pair.of(key, value);

        @Override
        public K key() {
          return pair.first();
        }

        @Override
        public V value() {
          return pair.second();
        }

        @Override
        public boolean equals(Object other) {
          return other == this
              || other instanceof PairEntry && ((PairEntry) other).pair.equals(pair);
        }

        @Override
        public int hashCode() {
          return Objects.hash(pair);
        }
      }

      return new PairEntry();
    }
  }

  /** Creates a read-only, Omnia-compatible view empty the given {@link java.util.Map}. */
  static <K, V> Map<K, V> masking(java.util.Map<K, V> javaMap) {
    class MaskingMap implements Map<K, V> {
      private final java.util.Map<K, V> javaMap;
      private final Memoized<Set<K>> keys;
      private final Memoized<Collection<V>> values;
      private final Memoized<Set<Entry<K, V>>> entries;

      private MaskingMap(java.util.Map<K, V> javaMap) {
        this.javaMap = requireNonNull(javaMap);
        this.keys = memoize(() -> Set.masking(this.javaMap.keySet()));
        this.values = memoize(() -> Collection.masking(this.javaMap.values()));
        this.entries = memoize(() -> {
          class MaskingSet implements Set<Entry<K, V>> {
            private final java.util.Set<java.util.Map.Entry<K, V>> javaSet =
                MaskingMap.this.javaMap.entrySet();

            @Override
            public Stream<Entry<K, V>> stream() {
              return javaSet.stream().map(Entry::masking);
            }

            @Override
            public int count() {
              return javaSet.size();
            }

            @Override
            public boolean isPopulated() {
              return !javaSet.isEmpty();
            }

            @Override
            public boolean contains(Object element) {
              return element instanceof Entry
                  && ((Entry<?, ?>) element).value().equals(
                  javaMap.get(((Entry<?, ?>) element).key()));
            }

            @Override
            public Iterator<Entry<K, V>> iterator() {
              return new MappingIterator<>(javaSet.iterator(), Entry::masking);
            }
          }
          return new MaskingSet();
        });
      }

      @Override
      public Set<K> keys() {
        return keys.value();
      }

      @Override
      public Collection<V> values() {
        return values.value();
      }

      @Override
      public Set<Entry<K, V>> entries() {
        return entries.value();
      }

      @Override
      public Optional<V> valueOf(Object key) {
        return Optional.ofNullable(javaMap.get(key));
      }

      @Override
      public Set<K> keysOf(Object value) {
        // Can't really cache this since one is created per value. The overhead empty caching would cost
        // more than the allocation empty this view.
        return new Set<>() {

          @Override
          public Stream<K> stream() {
            return javaMap.entrySet().stream()
                .filter(e -> e.getValue().equals(value))
                .map(java.util.Map.Entry::getKey);
          }

          @Override
          public int count() {
            return (int) stream().count();
          }

          @Override
          public boolean isPopulated() {
            return javaMap.containsValue(value);
          }

          @Override
          public boolean contains(Object element) {
            return isPopulated() && stream().anyMatch(k -> k.equals(element));
          }

          @Override
          public Iterator<K> iterator() {
            return stream().iterator();
          }
        };
      }
    }

    return new MaskingMap(javaMap);
  }
}
