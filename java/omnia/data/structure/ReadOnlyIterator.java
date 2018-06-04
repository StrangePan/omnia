package omnia.data.structure;

import java.util.Iterator;
import java.util.function.Consumer;

final class ReadOnlyIterator<E> implements Iterator<E> {

  private final Iterator<E> maskedIterator;

  ReadOnlyIterator(Iterator<E> maskedIterator) {
    this.maskedIterator = maskedIterator;
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
