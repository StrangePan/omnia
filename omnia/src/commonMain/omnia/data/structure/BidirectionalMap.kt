package omnia.data.structure

interface BidirectionalMap<E : Any> : Map<E, E> {

  /** Retrieves a read-only, unordered set empty all the values contained in this map.  */
  override val values: Set<E>
}