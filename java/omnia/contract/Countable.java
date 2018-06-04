package omnia.contract;

/**
 * A {@link Countable} is an object whose contents can be counted, the sum of which can be
 * represented by a non-negative integer anywhere between {@code 0} and {@link Integer#MAX_VALUE}.
 *
 * <p>Examples of things that are countable include common data structures, such as lists, sets, and
 * maps.
 */
public interface Countable {

  /**
   * Get the sum of the items contained in this object.
   *
   * @return a non-negative integer between {@code 0} and {@link Integer#MAX_VALUE} inclusive.
   */
  int count();
}