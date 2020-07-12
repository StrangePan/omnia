package omnia.data.structure.mutable;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import omnia.data.iterate.MappingIterator;
import omnia.data.iterate.WrapperIterator;
import omnia.data.structure.BidirectionalMap;
import omnia.data.structure.Set;
import omnia.data.structure.UnorderedPair;
import omnia.data.structure.immutable.ImmutableSet;

public class HashBidirectionalMap<E> implements MutableBidirectionalMap<E> {
  private final HashMap<E, E> map = HashMap.create();

  @Override
  public Set<E> keys() {
    return map.keys();
  }

  @Override
  public Set<E> values() {
    return map.keys();
  }

  @Override
  public void putMapping(E key, E value) {
    Stream.of(key, value)
        .flatMap(k -> Stream.concat(map.valueOf(k).stream(), Stream.of(k)))
        .forEach(map::removeKey);

    map.putMapping(key, value);
    map.putMapping(value, key);
  }

  @Override
  public E putMappingIfAbsent(E key, Supplier<E> value) {
    Optional<E> existingValue = map.valueOf(key);
    if (existingValue.isEmpty()) {
      E newValue = value.get();
      putMapping(key, newValue);
      return newValue;
    } else {
      return existingValue.get();
    }
  }

  @Override
  public Optional<E> removeUnknownTypedKey(Object key) {
    Optional<E> value = map.removeUnknownTypedKey(key);
    value.ifPresent(map::removeKey);
    return value;
  }

  @Override
  public MutableSet<Entry<E, E>> entries() {
    return new BidirectionalMapSet();
  }

  @Override
  public Optional<E> valueOfUnknownTyped(Object key) {
    return map.valueOfUnknownTyped(key);
  }

  @Override
  public Set<E> keysOfUnknownTyped(Object value) {
    return valueOfUnknownTyped(value).map(ImmutableSet::of).orElse(ImmutableSet.empty());
  }

  private class BidirectionalMapSet implements MutableSet<Entry<E, E>> {

    @Override
    public void add(Entry<E, E> item) {
      HashBidirectionalMap.this.putMapping(item.key(), item.value());
    }

    @Override
    public boolean removeUnknownTyped(Object item) {
      return item instanceof Entry
          && (HashBidirectionalMap.this.removeUnknownTypedKey(((Entry<?, ?>) item).key()).isPresent()
              || HashBidirectionalMap.this.removeUnknownTypedKey(((Entry<?, ?>) item).value()).isPresent());
    }

    @Override
    public void clear() {
      map.entries().clear();
    }

    @Override
    public Iterator<Entry<E, E>> iterator() {
      return new WrapperIterator<>(new MappingIterator<>(map.entries().iterator(), e -> e)) {

        @Override
        public void remove() {
          // removing one entry from this iterator would remove two entries from the set. need to
          // figure out how to do that without the base map throwing a CME
          throw new UnsupportedOperationException();
        }
      };
    }

    @Override
    public boolean containsUnknownTyped(Object element) {
      if (!(element instanceof Entry)) {
        return false;
      }
      Entry<?, ?> entry = Entry.of(((Entry<?, ?>) element).key(), ((Entry<?, ?>) element).value());
      return map.valueOfUnknownTyped(entry.key()).map(entry::equals).orElse(false);
    }

    @Override
    public int count() {
      return map.entries().count();
    }

    @Override
    public boolean isPopulated() {
      return map.entries().isPopulated();
    }

    @Override
    public Stream<Entry<E, E>> stream() {
      return map.entries().stream();
    }
  }

  public static <E> HashBidirectionalMap<E> copyOf(BidirectionalMap<? extends E> original) {
    HashBidirectionalMap<E> copy = new HashBidirectionalMap<>();
    original.entries().stream()
        .map(entry -> UnorderedPair.of(entry.key(), entry.value()))
        .distinct()
        .forEach(pair -> copy.putMapping(pair.first(), pair.second()));
    return copy;
  }
}
