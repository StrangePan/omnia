package omnia.data.structure.immutable;

import omnia.data.iterate.ReadOnlyIterator;
import omnia.data.structure.Set;
import omnia.data.structure.mutable.HashSet;
import omnia.data.structure.mutable.MutableSet;

import java.util.Iterator;
import java.util.stream.Stream;

public final class ImmutableSet<E> implements Set<E> {
  private final Set<E> elements;

  private ImmutableSet(Builder<E> builder) {
    MutableSet<E> tempSet = new HashSet<>();
    for (E element : builder.elements) {
      tempSet.add(element);
    }
    this.elements = tempSet;
  }

  @Override
  public Iterator<E> iterator() {
    return new ReadOnlyIterator<>(elements.iterator());
  }

  @Override
  public boolean contains(Object element) {
    return elements.contains(element);
  }

  @Override
  public boolean isPopulated() {
    return elements.isPopulated();
  }

  @Override
  public int count() {
    return elements.count();
  }

  @Override
  public Stream<E> stream() {
    return elements.stream();
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof ImmutableSet)) {
      return false;
    }
    ImmutableSet<?> otherSet = (ImmutableSet<?>) other;
    if (otherSet.count() != count()) {
      return false;
    }
    for (Object element : elements) {
      if (!otherSet.contains(element)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    return hashCode.value();
  }

  private final MemoizedInt hashCode = memoize(this::computeHash);

  private int computeHash() {
    int[] elementCodes = new int[count()];
    int i = 0;
    for (E element : elements) {
      elementCodes[i++] = element.hashCode();
    }
    Arrays.sort(elementCodes);
    return Objects.hash(Arrays.hashCode(elementCodes));
  }

  public static <E> Builder<E> builder() {
    return new Builder<>();
  }

  public static class Builder<E> extends AbstractBuilder<E, Builder<E>, ImmutableSet<E>> {

    @Override
    public ImmutableSet<E> build() {
      return new ImmutableSet<>(this);
    }

    @Override
    protected Builder<E> getSelf() {
      return this;
    }
  }
}
