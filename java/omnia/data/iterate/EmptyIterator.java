package omnia.data.iterate;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

public final class EmptyIterator<E> implements Iterator<E> {
  private static final EmptyIterator<?> EMPTY_ITERATOR = new EmptyIterator<>();

  public static <E> EmptyIterator<E> create() {
    @SuppressWarnings("unchecked")
    EmptyIterator<E> iterator = (EmptyIterator<E>) EMPTY_ITERATOR;
    return iterator;
  }

  private EmptyIterator() {}

  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public E next() {
    throw new NoSuchElementException("iterator is empty");
  }

  @Override
  public void remove() {
    throw new IllegalStateException("iterator is empty");
  }

  @Override
  public void forEachRemaining(Consumer<? super E> action) {
    Objects.requireNonNull(action);
  }
}
