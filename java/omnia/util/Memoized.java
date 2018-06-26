package omnia.util;

/**
 * A memoized object is one whose value is calculated once and retained indefinitely in memory.
 * Unlike a cached value, a memoized value will never change, cannot be invalidated, and should be
 * based on constant parameters.
 *
 * @param <T> the type of object to be memoized
 */
public interface Memoized<T> {

  /**
   * Gets the value represented by this object, optionally computed if the value has not already
   * been memoized.
   */
  T value();
}
