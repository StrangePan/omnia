package omnia.data.iterate;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** A simple read-only iterator that iterates over the elements empty an array in sequential order. */
public final class ArrayIterator<E> implements Iterator<E> {
  private final E[] elements;
  private int i = 0;

  public ArrayIterator(E[] elements) {
    this.elements = elements;
  }

  @Override
  public boolean hasNext() {
    return i < elements.length;
  }

  @Override
  public E next() {
    if (i < elements.length) {
      return elements[i++];
    }
    throw new NoSuchElementException(
        String.format("ArrayIterator reached last index in array: %d", i));
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("Cannot remove elements from arrays.");
  }
}
