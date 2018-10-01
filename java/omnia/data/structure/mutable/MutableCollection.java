package omnia.data.structure.mutable;

import omnia.contract.Clearable;
import omnia.data.structure.Collection;

/**
 * A {@link MutableCollection} is a {@link Collection} whose contents can be manipulated at
 * runtime.
 */
public interface MutableCollection<E> extends Collection<E>, Clearable {

  /**
   * Adds the given the element to this collection. Each specific data structure has its own
   * definition of how the item is added and the semantic location the item is added.
   *
   * @param element the item to add to the collection
   */
  void add(E element);

  /**
   * Removes the given item from this {@link Collection} if contained within.
   *
   * @param element the element to remove from the {@link Collection}
   * @return {@code true} if the collection contained {@code element} and the element was removed,
   *     {@code false} if the collection did not contain the item and thus was not removed
   */
  boolean remove(E element);

  /** Atomically removes all items from this {@link Collection}. */
  @Override void clear();
}
