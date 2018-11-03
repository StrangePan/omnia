package omnia.data.iterate;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.function.Consumer;

public abstract class WrapperIterator<E> implements Iterator<E> {
  private final Iterator<E> baseIterator;
  private boolean hasCurrent = false;
  private E current;

  protected WrapperIterator(Iterator<E> baseIterator) {
    this.baseIterator = requireNonNull(baseIterator);
  }

  protected final E current() {
    if (!hasCurrent) {
      throw new IllegalStateException(
          "Attempted to get current item from iterator before next() was called");
    }
    return current;
  }

  protected final boolean hasCurrent() {
    return hasCurrent;
  }

  @Override
  public boolean hasNext() {
    return baseIterator.hasNext();
  }

  @Override
  public E next() {
    this.current = baseIterator.next();
    hasCurrent = true;
    return current;
  }

  @Override
  public void remove() {
    baseIterator.remove();
    current = null;
    hasCurrent = false;
  }

  @Override
  public void forEachRemaining(Consumer<? super E> action) {
    baseIterator.forEachRemaining(action);
  }
}
