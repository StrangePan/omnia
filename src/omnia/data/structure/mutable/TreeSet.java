package omnia.data.structure.mutable;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.Optional;

public final class TreeSet<E> extends MaskingSet<E, java.util.TreeSet<E>> {

  public static <E> TreeSet<E> create(Comparator<? super E> comparator) {
    requireNonNull(comparator);
    return new TreeSet<E>(comparator);
  }

  private TreeSet(Comparator<? super E> comparator) {
    super(new java.util.TreeSet<>(comparator));
  }

  public Optional<E> itemPreceding(E other) {
    return Optional.ofNullable(javaSet().higher(other));
  }

  public Optional<E> itemFollowing(E other) {
    return Optional.ofNullable(javaSet().lower(other));
  }
}