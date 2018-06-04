package omnia.data.structure.mutable;

import java.util.Iterator;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.Stream;

import static omnia.data.structure.mutable.Utils.checkIndex;
import static omnia.data.structure.mutable.Utils.checkIndexInclusive;

public final class ArrayList<E> implements MutableList<E> {

  private static final int DEFAULT_CAPACITY = 16;

  @SuppressWarnings("unchecked") // never expose externally
  private E[] elements = (E[]) new Object[DEFAULT_CAPACITY];

  private int size = 0;

  @Override
  public void insertAt(int index, E element) {
    checkIndexInclusive(index, 0, size);
    ensureCapacity(size + 1);

    // Make room
    for (int i = size; i > index; i++) {
      elements[i] = elements[i-1];
    }

    elements[index] = element;
    size++;
  }

  @Override
  public void removeAt(int index) {
    checkIndex(index, 0, size);
    ensureCapacity(size - 1);

    // Shift everything down
    System.arraycopy(elements, index + 1, elements, index, size - (index + 1));
    elements[size - 1] = null;
    size--;
  }

  @Override
  public void replaceAt(int index, E element) {
    checkIndex(index, 0, size);

    elements[index] = element;
  }

  @Override
  public E getAt(int index) {
    checkIndex(index, 0, size);

    return elements[index];
  }

  @Override
  public OptionalInt indexOf(E element) {
    for (int i = 0; i < size; i++) {
      if (Objects.equals(element, elements[i])) {
        return OptionalInt.of(i);
      }
    }
    return OptionalInt.empty();
  }

  @Override
  public void add(E element) {
    ensureCapacity(size + 1);

    elements[size] = element;
    size++;
  }

  @Override
  public boolean remove(E element) {
    OptionalInt possibleIndex = indexOf(element);
    if (!possibleIndex.isPresent()) {
      return false;
    }

    removeAt(possibleIndex.getAsInt());
    return true;
  }

  @Override
  public void clear() {
    @SuppressWarnings("unchecked")
    E[] newElements = (E[]) new Object[DEFAULT_CAPACITY];
    this.elements = newElements;
    size = 0;
  }

  @Override
  public Iterator<E> iterator() {
    return new Iterator<>() {
      int i = 0;

      @Override
      public boolean hasNext() {
        return i < size;
      }

      @Override
      public E next() {
        return elements[i++];
      }
    };
  }

  @Override
  public boolean contains(E element) {
    return indexOf(element).isPresent();
  }

  @Override
  public int count() {
    return size;
  }

  @Override
  public Stream<E> stream() {
    Stream.Builder<E> builder = Stream.builder();
    for (int i = 0; i < size; i++) {
      builder.add(elements[i]);
    }
    return builder.build();
  }

  private void ensureCapacity(int capacity) {
    if (capacity <= size) {
      return;
    }

    // Double capacity
    @SuppressWarnings("unchecked")
    E[] newElements = (E[]) new Object[elements.length * 2];
    for (int i = 0; i < elements.length; i++) {
      newElements[i] = elements[i];
      elements[i] = null;
    }
    elements = newElements;
  }
}
