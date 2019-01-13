package omnia.data.structure;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
/**
 * A {@link HomogeneousPair} is a specific subclass of {@link Pair} in which both elements are of
 * the same type.
 *
 * <p>This type of {@link Pair} is useful in cases where the specific ordering of the items in the
 * pair are of no consequence.
 *
 * @param <E> the type of both elements contained in the pair
 */
public interface HomogeneousPair<E> extends Pair<E, E>, Collection<E> {

  static <E> HomogeneousPair<E> of(E first, E second) {
    class HomogeneousPairImpl implements HomogeneousPair<E> {
      private final E first;
      private final E second;

      private HomogeneousPairImpl(E first, E second) {
        this.first = first;
        this.second = second;
      }

      @Override
      public Iterator<E> iterator() {
        return List.of(first, second).iterator();
      }

      @Override
      public boolean contains(Object element) {
        return first.equals(element) || second.equals(element);
      }

      @Override
      public Stream<E> stream() {
        return Stream.of(first, second);
      }

      @Override
      public E first() {
        return first;
      }

      @Override
      public E second() {
        return second;
      }

      @Override
      public boolean isPopulated() {
        return true;
      }

      @Override
      public boolean equals(Object other) {
        if (!(other instanceof HomogeneousPairImpl)) {
          return false;
        }
        if (this == other) {
          return true;
        }

        HomogeneousPairImpl otherHomogeneousPair = (HomogeneousPairImpl) other;

        return this.first.equals(otherHomogeneousPair.first)
            && this.second.equals(otherHomogeneousPair.second);
      }

      @Override
      public int hashCode() {
        return Objects.hash(first, second);
      }
    }

    return new HomogeneousPairImpl(first, second);
  }
}
