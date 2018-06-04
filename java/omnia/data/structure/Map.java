package omnia.data.structure;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static omnia.data.stream.Collectors.toSet;

public interface Map<K, V> {

  Set<K> keys();

  Collection<V> values();

  Set<Entry<K, V>> entries();

  Optional<V> getValueOf(K key);

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
      public Optional<V> getValueOf(K key) {
        return javaMap.containsKey(key) ? Optional.of(javaMap.get(key)) : Optional.empty();
      }
    };
  }
}
