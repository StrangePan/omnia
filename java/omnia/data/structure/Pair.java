package omnia.data.structure;

import java.util.Objects;
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

  @Override
  default int count() {
    return 2;
  }

  static <E1, E2> Pair<E1, E2> of(E1 first, E2 second) {
    return new Pair<>() {
      @Override
      public E1 first() {
        return first;
      }

      @Override
      public E2 second() {
        return second;
      }

      @Override
      public boolean equals(Object other) {
        return other == this
            || other instanceof Pair
                && Objects.equals(((Pair) other).first(), first())
                && Objects.equals(((Pair) other).second(), second());
      }

      @Override
      public int hashCode() {
        return Objects.hash(first(), second());
      }
    };
  }
}
