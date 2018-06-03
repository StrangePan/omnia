package omnia.data.structure.immutable;

import java.util.Iterator;

final class ArrayIterator<E> implements Iterator<E> {
  private final E[] elements;
  private int i = 0;

  ArrayIterator(E[] elements) {
    this.elements = elements;
  }

  @Override
  public boolean hasNext() {
    return i < elements.length;
  }

  @Override
  public E next() {
    return elements[i++];
  }
}
