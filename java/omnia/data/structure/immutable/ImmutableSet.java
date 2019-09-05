package omnia.data.structure.immutable;

import static omnia.data.cache.MemoizedInt.memoize;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;
import omnia.data.cache.MemoizedInt;
import omnia.data.iterate.ReadOnlyIterator;
import omnia.data.structure.Collection;
import omnia.data.structure.Set;
import omnia.data.structure.mutable.HashSet;

import java.util.Iterator;
import java.util.stream.Stream;

public final class ImmutableSet<E> implements Set<E> {
  private final Set<E> elements;

  private ImmutableSet(Builder<E> builder) {
    this.elements = new HashSet<>(builder.elements, builder.equalsFunction, builder.hashFunction);
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
    private BiPredicate<Object, Object> equalsFunction = null;
    private ToIntFunction<Object> hashFunction = null;

    public Builder<E> equalsFunction(BiPredicate<Object, Object> equalsFunction) {
      this.equalsFunction = equalsFunction;
      return getSelf();
    }

    public Builder<E> hashFunction(ToIntFunction<Object> hashFunction) {
      this.hashFunction = hashFunction;
      return getSelf();
    }

    @Override
    public ImmutableSet<E> build() {
      return new ImmutableSet<>(this);
    }

    @Override
    protected Builder<E> getSelf() {
      return this;
    }
  }

  public static <E> ImmutableSet<E> copyOf(Collection<? extends E> collection) {
    if (collection instanceof ImmutableSet) {
      @SuppressWarnings("unchecked")
      ImmutableSet<E> s = (ImmutableSet<E>) collection;
      return s;
    }
    return ImmutableSet.<E>builder().addAll(collection).build();
  }
}
