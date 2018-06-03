package omnia.data.structure.immutable;

import omnia.data.structure.List;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Stream;

public final class ImmutableList<E> implements List<E> {

  private final E[] elements;

  private ImmutableList(Builder<E> builder) {
    @SuppressWarnings("unchecked") // The elements array must never be accessible externally.
    E[] elements = (E[]) builder.elements.toArray();
    this.elements = elements;
  }

  @Override
  public E getAt(int index) {
    if (index < 0 || index >= elements.length) {
      throw new IndexOutOfBoundsException(
          String.format("%d outside the range of [0,%d)", index, elements.length));
    }
    return elements[index];
  }

  @Override
  public Iterator<E> iterator() {
    return new ArrayIterator<>(elements);
  }

  @Override
  public boolean contains(E element) {
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

  public static <E> Builder<E> builder() {
    return new Builder<>();
  }

  public static final class Builder<E> extends AbstractBuilder<E, Builder<E>, ImmutableList<E>> {
    @Override
    public ImmutableList<E> build() {
      return new ImmutableList<>(this);
    }

    @Override
    protected Builder<E> getSelf() {
      return this;
    }
  }
}
