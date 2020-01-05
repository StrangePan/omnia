package omnia.data.structure;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import omnia.data.structure.immutable.ImmutableMap;
import omnia.data.structure.mutable.HashMap;
import omnia.data.structure.mutable.MutableMap;

public final class TypedMap {

  public static final class Key<V> {

    public static <V> Key<V> create() {
      return new Key<>();
    }
  }

  public static class Builder {
    private final MutableMap<Key<?>, Object> map = HashMap.create();

    public <V> Builder putMapping(Key<V> key, V value) {
      requireNonNull(key);
      requireNonNull(value);
      map.putMapping(key, value);
      return this;
    }

    public Builder putMappings(TypedMap map) {
      map.map.entries().forEach(entry -> this.map.putMapping(entry.key(), entry.value()));
      return this;
    }

    public Builder remove(Key<?> key) {
      requireNonNull(key);
      map.removeKey(key);
      return this;
    }

    public TypedMap build() {
      return new TypedMap(map);
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  public static TypedMap of() {
    return new TypedMap(ImmutableMap.empty());
  }

  public static <V> TypedMap of(Key<V> key, V value) {
    return new TypedMap(ImmutableMap.of(key, value));
  }

  private final Map<Key<?>, Object> map;

  private TypedMap(Map<Key<?>, Object> map) {
    this.map = ImmutableMap.copyOf(map);
  }

  public <V> Optional<V> get(Key<V> key) {
    @SuppressWarnings("unchecked")
    Optional<V> value = map.valueOf(key).map(v -> (V) v);
    return value;
  }

  public Builder toBuilder() {
    return new Builder().putMappings(this);
  }
}
