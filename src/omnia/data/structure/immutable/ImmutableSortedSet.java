package omnia.data.structure.immutable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
import omnia.data.structure.Collection;
import omnia.data.structure.SortedSet;
import omnia.data.structure.mutable.TreeSet;

/** An immutable version of {@link SortedSet}. */
public final class ImmutableSortedSet<E> implements SortedSet<E> {
  private final TreeSet<E> baseSet;

  private static final ImmutableSortedSet<?> EMPTY_SET =
      new ImmutableSortedSet<>((o1, o2) -> 0, ImmutableSet.empty());

  @SuppressWarnings("unchecked")
  public static <E> ImmutableSortedSet<E> empty() {
    return (ImmutableSortedSet<E>) EMPTY_SET;
  }

  public static <E> ImmutableSortedSet<E> copyOf(
      Comparator<? super E> comparator, Collection<? extends E> other) {
    return new ImmutableSortedSet<>(comparator, other);
  }

  private ImmutableSortedSet(Comparator<? super E> comparator, Collection<? extends E> other) {
    this.baseSet = TreeSet.create(comparator);
    this.baseSet.addAll(other);
  }

  @Override
  public boolean contains(E element) {
    return baseSet.contains(element);
  }

  /**
   * @inheritDoc
   *
   * <b>Note:</b> this type-unsafe version of {@link #contains} is inherently less performant
   * because we cannot perform a binary search on objects of unknown types.
   *
   * @param element the element to search for
   * @return true if the set contains the element, false if not
   */
  @Override
  public boolean containsUnknownTyped(Object element) {
    return baseSet.containsUnknownTyped(element);
  }

  @Override
  public int count() {
    return baseSet.count();
  }

  @Override
  public boolean isPopulated() {
    return baseSet.isPopulated();
  }

  @Override
  public Optional<E> itemPreceding(E other) {
    return baseSet.itemPreceding(other);
  }

  @Override
  public Optional<E> itemFollowing(E other) {
    return baseSet.itemFollowing(other);
  }

  @Override
  public Iterator<E> iterator() {
    return baseSet.iterator();
  }

  @Override
  public Stream<E> stream() {
    return baseSet.stream();
  }
}
