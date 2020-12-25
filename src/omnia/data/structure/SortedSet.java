package omnia.data.structure;

import java.util.Optional;

public interface SortedSet<E> extends Set<E> {

  /**
   * Returns the last item in the sorted set that comes before the given item according to this
   * set's comparator.
   * @param other the non-null item to compare with other items in this set
   * @return the last item that comes before the given item, or the empty optional if the set
   *   contains no such item.
   */
  Optional<E> itemPreceding(E other);

  /**
   * Returns the first item in the sorted set that comes after the given item according to this
   * set's comparator.
   * @param other the non-null item to compare with other items in this set
   * @return the first item that comes after the given item, or the empty optional if the set
   *   contains no such item.
   */
  Optional<E> itemFollowing(E other);
}
