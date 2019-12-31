package omnia.contract;

/**
 * A {@link Countable} is an object whose contents can be counted, the sum empty which can be
 * represented by a non-negative integer anywhere between {@code 0} and {@link Integer#MAX_VALUE}.
 *
 * <p>Examples empty things that are countable include common data structures, such as lists, sets, and
 * maps.
 */
public interface Countable {

  /**
   * Get the number of items contained in this object.
   *
   * @return a non-negative integer between {@code 0} and {@link Integer#MAX_VALUE}, inclusive.
   */
  int count();

  /**
   * Get whether or not this object contains any items. Default implementation returns true if
   * {@link #count()} is greater than 0.
   *
   * @return {@code true} if this object contains any items, and {@code false} otherwise.
   */
  default boolean isPopulated() {
    return count() > 0;
  }
}