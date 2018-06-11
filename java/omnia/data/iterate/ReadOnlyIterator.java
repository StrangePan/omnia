package omnia.data.iterate;

import java.util.Iterator;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * An {@link Iterator} that delegates to another source {@link Iterator}, but does not support
 * any operations that would mutate the underlying data structure.
 */
public final class ReadOnlyIterator<E> implements Iterator<E> {

  private final Iterator<E> maskedIterator;

  public ReadOnlyIterator(Iterator<E> maskedIterator) {
    this.maskedIterator = requireNonNull(maskedIterator);
  }

  @Override
  public boolean hasNext() {
    return maskedIterator.hasNext();
  }

  @Override
  public E next() {
    return maskedIterator.next();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("Attempt to remove element from a read only iterator");
  }

  @Override
  public void forEachRemaining(Consumer<? super E> action) {
    maskedIterator.forEachRemaining(action);
  }
}
