package omnia.data.structure;

public interface BidirectionalMap<K, V> extends Map<K, V> {

  BidirectionalMap<V, K> inverse();

  /** Retrieves a read-only, unordered set of all the values contained in this map. */
  @Override Set<V> values();
}
