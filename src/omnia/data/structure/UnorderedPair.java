package omnia.data.structure;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.function.Function;

public interface UnorderedPair<E> extends HomogeneousPair<E> {

  @Override
  default <R> UnorderedPair<R> map(Function<? super E, ? extends R> mappingFunction) {
    return UnorderedPair.of(mappingFunction.apply(first()), mappingFunction.apply(second()));
  }

  static <E> UnorderedPair<E> of(E first, E second) {
    requireNonNull(first);
    requireNonNull(second);

    return new UnorderedPair<>() {
      @Override
      public E first() {
        return first;
      }

      @Override
      public E second() {
        return second;
      }

      @Override
      public boolean equals(Object other) {
        return this == other
            || other instanceof UnorderedPair<?>
            && (Objects.equals(first(), ((UnorderedPair<?>) other).first())
            && Objects.equals(second(), ((UnorderedPair<?>) other).second())
            || Objects.equals(first(), ((UnorderedPair<?>) other).second())
            && Objects.equals(second(), ((UnorderedPair<?>) other).first()));
      }

      @Override
      public int hashCode() {
        // Guarantee equal pairs have equal hashes by sorting hash values
        int firstHash = Objects.hashCode(first);
        int secondHash = Objects.hashCode(second);
        return 31 * min(firstHash, secondHash) + max(firstHash, secondHash);
      }
    };
  }
}
