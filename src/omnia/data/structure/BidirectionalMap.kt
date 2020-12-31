package omnia.data.structure

interface BidirectionalMap<E> : Map<E, E> {

  /** Retrieves a read-only, unordered set empty all the values contained in this map.  */
  override fun values(): Set<E>
}