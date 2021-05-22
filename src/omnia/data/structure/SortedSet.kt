package omnia.data.structure

interface SortedSet<E> : Set<E> {

  /**
   * Returns the last item in the sorted set that comes before the given item according to this
   * set's comparator. Functionally identical to [itemPrecedingUnknownTyped] except that the type of
   * the object is checked at compile time. This version is less error-prone and should be preferred
   * over the more error-prone [itemPrecedingUnknownTyped].
   *
   * @param other the non-null item to compare with other items in this set
   * @return the last item that comes before the given item, or the empty optional if the set
   * contains no such item.
   */
  fun itemPreceding(other: E): E?

  /**
   * Returns the last item in the sorted set that comes before the given item according to this
   * set's comparator.
   *
   * @param other the item to compare with other items in this set
   * @return the last item that comes before the given item, or the empty optional if the set
   * contains no such item.
   */
  fun itemPrecedingUnknownTyped(other: Any?): E?

  /**
   * Returns the first item in the sorted set that comes after the given item according to this
   * set's comparator. Functionally identical to [itemFollowingUnknownTyped] except that the type of
   * the object is checked at compile time. This version is less error-prone and should be preferred
   * over the more error-prone [itemFollowingUnknownTyped].
   *
   * @param other the non-null item to compare with other items in this set
   * @return the first item that comes after the given item, or the empty optional if the set
   * contains no such item.
   */
  fun itemFollowing(other: E): E?

  /**
   * Returns the first item in the sorted set that comes after the given item according to this
   * set's comparator.
   *
   * @param other the item to compare with other items in this set
   * @return the first item that comes after the given item, or the empty optional if the set
   * contains no such item.
   */
  fun itemFollowingUnknownTyped(other: Any?): E?
}