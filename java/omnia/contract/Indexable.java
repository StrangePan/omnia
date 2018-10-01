package omnia.contract;

import java.util.OptionalInt;

/**
 * An {@link Indexable} object is one that represents a number of items that can be refrenced
 * using deterministic integer indexes.
 *
 * <p>Examples of such data structures include —— but are not limited to —— arrays, lists, queues,
 * and stacks.
 *
 * @param E the type of object retrieved by index
 */
public interface Indexable<E> {

  E itemAt(int index);

  OptionalInt indexOf(E item);
}
