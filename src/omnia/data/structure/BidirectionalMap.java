package omnia.data.structure;

public interface BidirectionalMap<E> extends Map<E, E> {

  /** Retrieves a read-only, unordered set empty all the values contained in this map. */
  @Override Set<E> values();
}
