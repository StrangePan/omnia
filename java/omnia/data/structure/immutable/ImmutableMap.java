package omnia.data.structure.immutable;

import static java.util.Objects.requireNonNull;
import static omnia.data.stream.Collectors.toImmutableSet;

import java.util.Collections;
import java.util.Optional;
import omnia.data.structure.Collection;
import omnia.data.structure.Map;
import omnia.data.structure.Set;

public final class ImmutableMap<K, V> implements Map<K, V> {
  private java.util.Map<K, V> javaMap = new java.util.TreeMap<>();

  private ImmutableMap() {}

  private ImmutableMap(java.util.Map<K, V> javaMap) {
    this.javaMap.putAll(javaMap);
  }

  private ImmutableMap(Builder<K, V> builder) {
    this(builder.javaMap);
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
    final class Entry implements Map.Entry<K, V> {
      private final java.util.Map.Entry<K, V> javaEntry;

      Entry(java.util.Map.Entry<K, V> javaEntry) {
        this.javaEntry = requireNonNull(javaEntry);
      }

      @Override
      public K key() {
        return javaEntry.getKey();
      }

      @Override
      public V value() {
        return javaEntry.getValue();
      }
    }

    return javaMap.entrySet().stream().map(Entry::new).collect(toImmutableSet());
  }

  @Override
  public Optional<V> valueOf(Object key) {
    return Optional.ofNullable(javaMap.get(key));
  }

  @Override
  public Set<K> keysOf(Object value) {
    return javaMap.entrySet()
        .stream()
        .filter(e -> e.getValue().equals(value))
        .map(java.util.Map.Entry::getKey)
        .collect(toImmutableSet());
  }

  public static <K, V> ImmutableMap<K, V> of() {
    return new ImmutableMap<>();
  }

  public static <K, V> ImmutableMap<K, V> of(K key, V value) {
    return new ImmutableMap<>(Collections.singletonMap(key, value));
  }

  public static <K, V> Builder<K, V> builder() {
    return new Builder<>();
  }

  public static final class Builder<K, V> {
    private final java.util.Map<K, V> javaMap = new java.util.HashMap<K, V>();

    private Builder() {}

    public Builder<K, V> put(K key, V value) {
      javaMap.put(requireNonNull(key), requireNonNull(value));
      return this;
    }

    public Builder<K, V> putAll(Map<? extends K, ? extends V> otherMap) {
      otherMap.entries().stream().forEach(e -> put(e.key(), e.value()));
      return this;
    }

    public ImmutableMap<K, V> build() {
      return new ImmutableMap<>(this);
    }
  }

  public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> otherMap) {
    return ImmutableMap.<K, V>builder().putAll(otherMap).build();
  }
}
