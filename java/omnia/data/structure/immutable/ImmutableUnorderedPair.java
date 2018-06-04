package omnia.data.structure.immutable;

import omnia.data.structure.HomogeneousPair;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Objects.requireNonNull;

public final class ImmutableUnorderedPair<E> implements HomogeneousPair<E> {

  private final E first;
  private final E second;

  public static <E> ImmutableUnorderedPair<E> of(E first, E second) {
    return new ImmutableUnorderedPair<>(requireNonNull(first), requireNonNull(second));
  }

  private ImmutableUnorderedPair(E first, E second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public Iterator<E> iterator() {
    return List.of(first, second).iterator();
  }

  @Override
  public boolean contains(E element) {
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
  public int count() {
    return 2;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof ImmutableUnorderedPair)) {
      return false;
    }
    if (this == other) {
      return true;
    }

    ImmutableUnorderedPair<?> otherUnorderedPair = (ImmutableUnorderedPair<?>) other;

    if (this.first.equals(otherUnorderedPair.first)) {
      return this.second.equals(otherUnorderedPair.second);
    }
    return this.first.equals(otherUnorderedPair.second)
        && this.second.equals(otherUnorderedPair.first);
  }

  @Override
  public int hashCode() {
    // Guarantee equal pairs have equal hashes by sorting hash values
    int firstHash = Objects.hashCode(first);
    int secondHash = Objects.hashCode(second);
    return 31 * min(firstHash, secondHash) + max(firstHash, secondHash);
  }
}
