package omnia.data.structure.mutable;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

public final class LinkedQueue<E> implements Queue<E> {

  private final MutableList<E> items = new LinkedList<>();

  @Override
  public void enqueue(E item) {
    items.add(requireNonNull(item));
  }

  @Override
  public Optional<E> dequeue() {
    if (!items.isPopulated()) {
      return Optional.empty();
    }
    E item = items.itemAt(0);
    items.removeAt(0);
    return Optional.of(item);
  }

  @Override
  public Optional<E> peek() {
    return items.isPopulated() ? Optional.of(items.itemAt(0)) : Optional.empty();
  }

  @Override
  public int count() {
    return items.count();
  }

  @Override
  public boolean isPopulated() {
    return items.isPopulated();
  }
}
