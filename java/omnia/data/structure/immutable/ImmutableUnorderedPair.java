package omnia.data.structure.immutable;

import omnia.data.structure.HomogeneousPair;
import omnia.data.structure.Set;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Objects.requireNonNull;

/**
 * An immutable implementation of {@link HomogeneousPair} in which the order of the contents are
 * irrelevant in equality and hashing computations.
 *
 * <p>The {@link #equals(Object)} and {@link #hashCode()} implementations for this class
 * intentionally do not take order of objects into account, allowing this to represent pairs of
 * items where order absolutely does not matter and can be used in a {@link Set}.
 *
 * <p>This class does not support null values. Passing in {@code null} into any constructors will
 * result in a {@link NullPointerException} being thrown.
 *
 * @param E the type of item contained in this pair
 */
public final class ImmutableUnorderedPair<E> implements HomogeneousPair<E> {

  private final E first;
  private final E second;

  /**
   * Static constructor for a new {@link ImmutableUnorderedPair}.
   *
   * @param first the first item in the pair
   * @param second the second item in the pair
   * @throws NullPointerException if either parameter is null
   */
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

  /**
   * Evaluates equivalency with another {@link ImmutableUnorderedPair}.
   *
   * <p>In addition to the {@link Object#equals(Object)} contract (see below), this implementation
   * does not take order of items into account when evaluating equivalency. This means that two
   * {@link ImmutableUnorderedPair}s are considered equal if they contain equivalent items, even if
   * the order of these items differ.
   *
   * <p>This method returns {@code false} if the given object is not an {@link
   * ImmutableUnorderedPair} instance.
   *
   * <p>{@inheritDoc}
   */
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

  /**
   * Computes a hash code for this pair of objects, recursively computing the hash codes of the
   * items contained.
   *
   * <p>Like the {@link #equals(Object)} method, this {@link #hashCode()} implementation disregards
   * item ordering by objectively ordering the contained item's hash codes according to a
   * deterministic heuristic. This means that two {@link ImmutableUnorderedPair}s will have the
   * same hash code if they contain equivalent items, even if the order of these items differ. This
   * makes this class compatible with data structures that depend on the {@link Object#hashCode()}
   * method.
   *
   * <p>{@inheritDoc}
   */
  @Override
  public int hashCode() {
    // Guarantee equal pairs have equal hashes by sorting hash values
    int firstHash = Objects.hashCode(first);
    int secondHash = Objects.hashCode(second);
    return 31 * min(firstHash, secondHash) + max(firstHash, secondHash);
  }
}
