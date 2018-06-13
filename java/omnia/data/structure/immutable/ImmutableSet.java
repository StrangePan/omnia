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
  public boolean contains(E element) {
    return elements.contains(element);
  }

  @Override
  public int count() {
    return elements.count();
  }

  @Override
  public Stream<E> stream() {
    return elements.stream();
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
