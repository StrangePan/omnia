package omnia.data.structure.immutable;

import omnia.data.structure.Set;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

public final class ImmutableSet<E> implements Set<E> {
  private final E[] elements;

  private ImmutableSet(Builder<E> builder) {
    java.util.Set<E> tempSet = new HashSet<>();
    tempSet.addAll(builder.elements);

    @SuppressWarnings("unchecked") // elements must never be accessible externally
    E[] elements = (E[]) tempSet.toArray();
    this.elements = elements;
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
