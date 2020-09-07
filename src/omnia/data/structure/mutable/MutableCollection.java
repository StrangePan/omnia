package omnia.data.structure.mutable;

import java.util.Iterator;
import java.util.stream.Stream;
import omnia.contract.Clearable;
import omnia.data.structure.Collection;

/**
 * A {@link MutableCollection} is a {@link Collection} whose contents can be manipulated at
 * runtime.
 */
public interface MutableCollection<E> extends Collection<E>, Clearable {

  /**
   * Adds the given the element to this collection. Each specific data structure has its own
   * definition of how the items are added and the semantic location the items added.
   *
   * @param element the item to add to the collection
   */
  void add(E element);

  /**
   * Adds all items in the given collection to this one. Each specific data structure has its own
   * definition of how the items are added and the semantic location the items added.
   *
   * @param elements the items to add to the collection
   */
  void addAll(Collection<? extends E> elements);

  /**
   * Removes the given item from this {@link Collection} if contained within.
   *
   * For collections that contain duplicates of an item, this method removes at most one duplicate
   * of that item. Callers wishing to remove all such items will need to call this function multiple
   * times.
   *
   * @param element the element to remove from the {@link Collection}
   * @return {@code true} if the collection contained {@code element} and the element was removed,
   *     {@code false} if the collection did not contain the item and thus was not removed
   */
  default boolean remove(E element) {
    return removeUnknownTyped(element);
  }

  /**
   * Removes the given item from this {@link Collection} if contained within.
   *
   * For collections that contain duplicates of an item, this method removes at most one duplicate
   * of that item. Callers wishing to remove all such items will need to call this function multiple
   * times.
   *
   * @param element the element to remove from the {@link Collection}
   * @return {@code true} if the collection contained {@code element} and the element was removed,
   *     {@code false} if the collection did not contain the item and thus was not removed
   */
  boolean removeUnknownTyped(Object element);

  /** Atomically removes all items from this {@link Collection}. */
  @Override void clear();

  static <E> MutableCollection<E> masking(java.util.Collection<E> javaCollection) {
    return new MutableCollection<>() {

      @Override
      public void add(E element) {
        javaCollection.add(element);
      }

      @Override
      public void addAll(Collection<? extends E> elements) {
        javaCollection.addAll(elements.stream().collect(java.util.stream.Collectors.toList()));
      }

      @Override
      public boolean removeUnknownTyped(Object element) {
        return javaCollection.remove(element);
      }

      @Override
      public void clear() {
        javaCollection.clear();
      }

      @Override
      public Stream<E> stream() {
        return javaCollection.stream();
      }

      @Override
      public int count() {
        return javaCollection.size();
      }

      @Override
      public boolean isPopulated() {
        return !javaCollection.isEmpty();
      }

      @Override
      public boolean containsUnknownTyped(Object element) {
        return javaCollection.contains(element);
      }

      @Override
      public Iterator<E> iterator() {
        return javaCollection.iterator();
      }
    };
  }
}
