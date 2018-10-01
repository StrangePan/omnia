package omnia.data.structure;

import omnia.contract.Countable;

/**
 * A {@link Pair} is an set of exactly two objects, best used to represent items with little
 * semantic correlation.
 *
 * @param <E1> the type of the first item
 * @param <E2> the type of the second item
 */
public interface Pair<E1, E2> extends Countable {

  /**
   * Gets the first item contained in the pair. Subsequent calls to this method should always
   * return the same value.
   */
  E1 first();

  /**
   * Gets the second item contained in the pair. Subsequent calls to this method should always
   * return the same value.
   */
  E2 second();
}
