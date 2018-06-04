package omnia.data.structure.mutable;

import java.util.Iterator;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.Stream;

import static omnia.data.structure.mutable.Utils.checkIndex;
import static omnia.data.structure.mutable.Utils.checkIndexInclusive;

public class LinkedList<E> implements MutableList<E> {

  private Node<E> first = null;
  private Node<E> last = null;
  private int size = 0;

  @Override
  public void insertAt(int index, E element) {
    checkIndexInclusive(index, 0, size);

    // Special case inserting at end of list
    if (index == size) {
      add(element);
      return;
    }

    // Special case inserting at start of list
    if (index == 0) {
      Node<E> node = new Node<>();
      node.element = element;
      node.next = first;
      first = node;
      size++;
      return;
    }

    // Traverse through list to find insertion point
    int i = 1;
    Node<E> node = first;
    while (i < index) {
      node = node.next;
      i++;
    }

    // Insert the node
    Node<E> newNode = new Node<>();
    newNode.element = element;
    newNode.next = node.next;
    node.next = newNode;
    size++;
  }

  @Override
  public void removeAt(int index) {
    checkIndex(index, 0, size);

    // Special case removing only item from list
    if (size == 1) {
      first = null;
      last = null;
      size = 0;
      return;
    }

    // Special case removing from front of list
    if (index == 0) {
      first = first.next;
      size--;
      return;
    }

    // Traverse list to find removal point
    Node<E> node = first;
    int i = 1;
    while (i < index) {
      node = node.next;
      i++;
    }

    // Remove the node
    // Special case removal from end of list
    if (index == size - 1) {
      last = node;
    }
    node.next = node.next.next;
    size--;
  }

  @Override
  public void replaceAt(int index, E element) {
    checkIndex(index, 0, size);

    // Traverse list to find removal point
    Node<E> node = first;
    int i = 0;
    while (i < index) {
      node = node.next;
      i++;
    }
    node.element = element;
  }

  @Override
  public E getAt(int index) {
    checkIndex(index, 0, size);

    // Traverse list to find removal point
    Node<E> node = first;
    int i = 0;
    while (i < index) {
      node = node.next;
      i++;
    }
    return node.element;
  }

  @Override
  public OptionalInt indexOf(E element) {
    int i = 0;
    for (Node<E> node = first; node != null; node = node.next) {
      if (Objects.equals(node.element, element)) {
        return OptionalInt.of(i);
      }
      i++;
    }
    return OptionalInt.empty();
  }

  @Override
  public void add(E element) {
    // Special case inserting the first item
    if (size == 0) {
      addFirstItem(element);
      return;
    }

    last.next = new Node<>();
    last = last.next;
    last.element = element;
  }

  @Override
  public boolean remove(E element) {
    if (size == 0) {
      return false;
    }

    // Special case the element being the first one
    if (Objects.equals(element, first.element)) {
      removeAt(0);
      return true;
    }

    for (Node<E> node = first; node.next != null; node = node.next) {
      if (Objects.equals(node.next.element, element)) {

        // Special case removing from end of list
        if (node.next == last) {
          last = node;
        }
        node.next = node.next.next;
        return true;
      }
    }
    return false;
  }

  @Override
  public void clear() {
    first = null;
    last = null;
    size = 0;
  }

  @Override
  public Iterator<E> iterator() {
    return new Iterator<>() {
      Node<E> current = first;

      @Override
      public boolean hasNext() {
        return current != null;
      }

      @Override
      public E next() {
        E element = current.element;
        current = current.next;
        return element;
      }
    };
  }

  @Override
  public boolean contains(E element) {
    for (Node<E> node = first; node != null; node = node.next) {
      if (Objects.equals(node.element, element)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int count() {
    return size;
  }

  @Override
  public Stream<E> stream() {
    Stream.Builder<E> builder = Stream.builder();
    for (Node<E> node = first; node != null; node = node.next) {
      builder.add(node.element);
    }
    return builder.build();
  }

  private void addFirstItem(E element) {
    if (size > 0 || first != null || last != null) {
      throw new IllegalStateException(
          String.format(
              "Attempted to add first item when list has already been initialized. size: %d",
              size));
    }

    Node<E> node = new Node<>();
    node.element = element;
    node.next = null;
    first = node;
    last = node;
    size = 1;
  }

  private static final class Node<E> {
    E element;
    Node<E> next;
  }
}
