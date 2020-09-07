package omnia.data.structure.mutable;

import java.util.Iterator;
import java.util.Optional;

/** A {@link Stack} implementation backed by arrays. */
public final class ArrayStack<E> implements Stack<E> {

  private final MutableList<E> list = ArrayList.create();

  public static <E> Stack<E> create() {
    return new ArrayStack<>();
  }

  private ArrayStack() {}

  @Override
  public void push(E item) {
    list.add(item);
  }

  @Override
  public Optional<E> pop() {
    return list.isPopulated() ? Optional.of(list.removeAt(list.count())) : Optional.empty();
  }

  @Override
  public Iterator<E> iterator() {
    return list.iterator();
  }

  @Override
  public int count() {
    return list.count();
  }
}
