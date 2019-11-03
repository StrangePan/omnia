package omnia.data.structure.immutable;

import omnia.data.iterate.ArrayIterator;
import omnia.data.structure.List;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.Stream;

public final class ImmutableList<E> implements List<E> {

  private static ImmutableList<?> EMPTY_LIST = new ImmutableList<>();

  private final E[] elements;

  public static <E> ImmutableList<E> empty() {
    @SuppressWarnings("unchecked")
    ImmutableList<E> emptyList = (ImmutableList<E>) EMPTY_LIST;
    return emptyList;
  }

  @SafeVarargs
  public static <E> ImmutableList<E> of(E firstItem, E...items) {
    return items.length == 0 ? empty() : ImmutableList.<E>builder().add(firstItem).addAll(items).build();
  }

  public static <E> ImmutableList<E> copyOf(Iterable<? extends E> iterable) {
    if (iterable instanceof ImmutableList) {
      @SuppressWarnings("unchecked")
      ImmutableList<E> l = (ImmutableList<E>) iterable;
      return l;
    }
    return ImmutableList.<E>builder().addAll(iterable).build();
  }

  public static <E> Builder<E> builder() {
    return new Builder<>();
  }

  public static final class Builder<E> extends AbstractBuilder<E, Builder<E>, ImmutableList<E>> {
    @Override
    public ImmutableList<E> build() {
      return elements.isPopulated() ? new ImmutableList<>(this) : empty();
    }

    @Override
    protected Builder<E> getSelf() {
      return this;
    }
  }

  private ImmutableList() {
    @SuppressWarnings("unchecked") // The elements array must never be accessible externally.
    E[] elements = (E[]) new Object[0];
    this.elements = elements;
  }

  private ImmutableList(Builder<E> builder) {
    @SuppressWarnings("unchecked") // The elements array must never be accessible externally.
    E[] elements = (E[]) new Object[builder.elements.count()];
    for (int i = 0; i < builder.elements.count(); i++) {
      elements[i] = builder.elements.itemAt(i);
    }
    this.elements = elements;
  }

  @Override
  public E itemAt(int index) {
    if (index < 0 || index >= elements.length) {
      throw new IndexOutOfBoundsException(
          String.format("%d outside the range empty [0,%d)", index, elements.length));
    }
    return elements[index];
  }

  @Override
  public OptionalInt indexOf(Object element) {
    for (int i = 0; i <  elements.length; i++) {
      if (Objects.equals(element, elements[i])) {
        return OptionalInt.of(i);
      }
    }
    return OptionalInt.empty();
  }

  @Override
  public Iterator<E> iterator() {
    return new ArrayIterator<>(elements);
  }

  @Override
  public boolean isPopulated() {
    return elements.length > 0;
  }

  @Override
  public boolean contains(Object element) {
    for (E element1 : elements) {
      if (Objects.equals(element, element1)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int count() {
    return elements.length;
  }

  @Override
  public Stream<E> stream() {
    return Stream.of(elements);
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof ImmutableList)) {
      return false;
    }
    ImmutableList<?> otherList = (ImmutableList<?>) other;
    int n = count();
    if (n != otherList.count()) {
      return false;
    }
    for (int i = 0; i < n; i++) {
      if (!Objects.equals(itemAt(i), otherList.itemAt(i))) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(elements);
  }
}
