package omnia.data.structure;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static omnia.data.stream.Collectors.toImmutableSet;

/** A {@link Map} is a data structure that associates unique keys to corresponding values. */
public interface Map<K, V> {

  /** Retrieves a read-only, unordered set of all of the keys contained in this map. */
  Set<K> keys();

  /** Retrieves a read-only, unordered collection of all the values contained in this map. */
  Collection<V> values();

  /** Retrieves a read-only, unordered set of all the entries contained in this map. */
  Set<Entry<K, V>> entries();

  /** Retrieves the value associated with the given key if it is contained in the map. */
  Optional<V> valueOf(Object key);

  /**
   * Retrieves the one or more keys associated with the given value. This reverse lookup is likely
   * to be far slower than the {@link #valueOf(K)} counterpart.
   */
  Set<K> keysOf(Object value);

  /** An {@link Entry} is read-only representing of a single key-value mapping.  */
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

  /** Creates a read-only, Omnia-compatible view of the given {@link java.util.Map}. */
  static <K, V> Map<K, V> masking(java.util.Map<K, V> javaMap) {
    return new Map<>() {

      @Override
      public Set<K> keys() {
        return Set.masking(javaMap.keySet());
      }

      @Override
      public Collection<V> values() {
        return Collection.masking(javaMap.values());
      }

      @Override
      public Optional<V> valueOf(Object key) {
        return javaMap.containsKey(key) ? Optional.of(javaMap.get(key)) : Optional.empty();
      }

      @Override
      public Set<K> keysOf(Object value) {
        return javaMap.entrySet()
            .stream()
            .filter(e -> Objects.equals(e.getValue(), value))
            .map(java.util.Map.Entry::getKey)
            .collect(toImmutableSet());
      }

      @Override
      public Set<Entry<K, V>> entries() {
        return javaMap.entrySet().stream().map(Entry::masking).collect(toImmutableSet());
      }
    };
  }
}
