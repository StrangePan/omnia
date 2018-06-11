package omnia.data.structure;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static omnia.data.stream.Collectors.toSet;

/** A {@link Map} is a data structure that associates keys to values. */
public interface Map<K, V> {

  /** Retrieves a read-only, unordered set of all of the keys contained in this map. */
  Set<K> keys();

  /** Retrieves a read-only, unordered collection of all the values contained in this map. */
  Collection<V> values();

  /** Retrieves a read-only, unordered set of every key-value pairing in the map. */
  Set<Entry<K, V>> entries();

  /** Retrieves the value associated with the given key if it is contained in the map. */
  Optional<V> valueOf(K key);

  /** An {@link Entry} is read-only representing of a single key-value mapping.  */
  interface Entry<K, V> {

    K key();

    V value();

    static <K, V> Entry<K, V> masking(java.util.Map.Entry<K, V> javaEntry) {
      class MaskedEntry implements Entry<K, V> {
        private final java.util.Map.Entry<K, V> jEntry = requireNonNull(javaEntry);

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
      };

      return new MaskedEntry();
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
      public Set<Entry<K, V>> entries() {
        return javaMap.entrySet().stream().map(Entry::masking).collect(toSet());
      }

      @Override
      public Optional<V> valueOf(K key) {
        return javaMap.containsKey(key) ? Optional.of(javaMap.get(key)) : Optional.empty();
      }
    };
  }
}
