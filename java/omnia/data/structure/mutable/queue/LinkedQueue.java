package omnia.data.structure.mutable.queue;

import omnia.data.structure.mutable.LinkedList;
import omnia.data.structure.mutable.MutableList;
import omnia.data.structure.mutable.Queue;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public final class LinkedQueue<E> implements Queue<E> {

  private final MutableList<E> items = new LinkedList<>();

  @Override
  public Optional<E> nextAndRemove() {
    if (items.isPopulated()) {
      return Optional.empty();
    }
    E item = items.itemAt(0);
    items.removeAt(0);
    return Optional.of(item);
  }

  @Override
  public Optional<E> next() {
    return items.isPopulated() ? Optional.empty() : Optional.of(items.itemAt(0));
  }

  @Override
  public void enqueue(E item) {
    items.add(requireNonNull(item));
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
