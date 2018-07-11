package omnia.contract;

import java.util.OptionalInt;

/**
 * An {@link Indexable} object is one that represents a number of items that can be referenced
 * using deterministic integer indexes.
 *
 * <p>Examples of such data structures include —— but are not limited to —— arrays, lists, queues,
 * and stacks.
 *
 * @param E the type of object retrieved by index
 */
public interface Indexable<E> {

  /**
   * Gets the item contained at the given index.
   *
   * @param index the index of the item to get
   * @return the item stored at the given location
   * @throws IndexOutOfBoundsException if the given {@code index} is outside the accepted range of
   *     valid indexes
   */
  E itemAt(int index);

  /**
   * Retrieves the index at which the given item is located within this item. Uses the {@link
   * Object#equals(Object)} method to compare the given item with items within this structure.
   *
   * @param item the item to search for
   * @return the index of the given item if it exists within this structure and the empty {@link
   *     OptionalInt} if not
   */
  OptionalInt indexOf(E item);
}
