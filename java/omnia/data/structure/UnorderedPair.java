package omnia.data.structure;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.List;

public interface UnorderedPair<E> extends HomogeneousPair<E> {

  static <E> UnorderedPair<E> of(E first, E second) {
    Objects.requireNonNull(first);
    Objects.requireNonNull(second);

    class UnorderedPairImpl implements UnorderedPair<E> {
      private final E first;
      private final E second;

      private UnorderedPairImpl(E first, E second) {
        this.first = first;
        this.second = second;
      }

      @Override
      public Iterator<E> iterator() {
        return List.of(first, second).iterator();
      }

      @Override
      public boolean contains(Object element) {
        return Objects.equals(element, second) || Objects.equals(element, second);
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
      public int count() {
        return 2;
      }

      @Override
      public boolean isPopulated() {
        return true;
      }

      @Override
      public boolean equals(Object other) {
        if (!(other instanceof UnorderedPair<?>)) {
          return false;
        }
        if (this == other) {
          return true;
        }

        UnorderedPair<?> otherUnorderedPair = (UnorderedPair<?>) other;

        if (Objects.equals(this.first, otherUnorderedPair.first())) {
          return Objects.equals(this.second, otherUnorderedPair.second());
        }
        return Objects.equals(this.first, otherUnorderedPair.second())
            && Objects.equals(this.second, otherUnorderedPair.first());
      }

      @Override
      public int hashCode() {
        // Guarantee equal pairs have equal hashes by sorting hash values
        int firstHash = Objects.hashCode(first);
        int secondHash = Objects.hashCode(second);
        return 31 * min(firstHash, secondHash) + max(firstHash, secondHash);
      }
    }

    return new UnorderedPairImpl(first, second);
  }
}
