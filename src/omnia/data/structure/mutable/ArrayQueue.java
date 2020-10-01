package omnia.data.structure.mutable;

import static java.lang.Math.max;
import static java.util.Objects.requireNonNull;

import java.util.Optional;

public class ArrayQueue<E> implements Queue<E> {

  private static final int INITIAL_CAPACITY = 16;

  private final int minimumCapacity;
  private FixedArrayQueue<E> subQueue;

  public static <E> ArrayQueue<E> create() {
    return new ArrayQueue<E>();
  }

  public static <E> ArrayQueue<E> createWithInitialCapacity(int capacity) {
    return new ArrayQueue<E>(capacity);
  }

  private ArrayQueue() {
    this(INITIAL_CAPACITY);
  }

  private ArrayQueue(int capacity) {
    if (capacity < 1) {
      throw new IllegalArgumentException(
          String.format("Capacity must be at least 1: %d given.", capacity));
    }
    minimumCapacity = capacity;
    this.subQueue = new FixedArrayQueue<>(capacity);
  }

  @Override
  public void enqueue(E item) {
    requireNonNull(item);
    if (subQueue.capacity() == subQueue.count()) {
      subQueue = new FixedArrayQueue<>(subQueue.capacity() * 2, subQueue);
    }
    subQueue.enqueue(item);
  }

  @Override
  public Optional<E> dequeue() {
    Optional<E> item = subQueue.dequeue();
    if (subQueue.capacity() > minimumCapacity && subQueue.count() <= subQueue.capacity() / 4) {
      subQueue = new FixedArrayQueue<>(max(subQueue.capacity() / 2 , minimumCapacity), subQueue);
    }
    return item;
  }

  @Override
  public Optional<E> peek() {
    return subQueue.peek();
  }

  @Override
  public int count() {
    return subQueue.count();
  }

  @Override
  public boolean isPopulated() {
    return subQueue.isPopulated();
  }

  private static final class FixedArrayQueue<E> implements Queue<E> {
    private final E[] items;
    private int head = 0;
    private int tail = 0;

    FixedArrayQueue(int capacity) {
      if (capacity < 1) {
        throw new IllegalArgumentException(
            String.format("Capacity must be at least 1 (%d given)", capacity));
      }

      @SuppressWarnings("unchecked")
      E[] items = (E[]) new Object[capacity];
      this.items = items;
    }

    FixedArrayQueue(int capacity, FixedArrayQueue<E> other) {
      this(capacity);
      if (other.count() > capacity) {
        throw new IllegalArgumentException(
            String.format(
                "Attempted to copy a queue (capacity %d) into a smaller queue (capacity %d)",
                other.capacity(),
                capacity));
      }
      for (Optional<E> item = other.dequeue(); item.isPresent(); item = other.dequeue()) {
        enqueue(item.get());
      }
    }

    @Override
    public void enqueue(E item) {
      requireNonNull(item);
      if (items[tail] != null) {
        throw new IllegalStateException(
            String.format("Attempted to enqueue an item into a full queue (size %d)", items.length));
      }

      items[tail] = item;
      tail = (tail + 1) % items.length;
    }

    @Override
    public Optional<E> dequeue() {
      E item = items[head];
      items[head] = null;

      if (item != null) {
        head = (head + 1) % items.length;
      }

      return Optional.ofNullable(item);
    }

    @Override
    public Optional<E> peek() {
      return Optional.ofNullable(items[head]);
    }

    @Override
    public int count() {
      if (head < tail) {
        return tail - head;
      }
      if (head > tail) {
        return tail + items.length - head;
      }
      if (items[tail] != null) {
        return items.length;
      }
      return 0;
    }

    @Override
    public boolean isPopulated() {
      return items[head] != null;
    }

    int capacity() {
      return items.length;
    }
  }
}
