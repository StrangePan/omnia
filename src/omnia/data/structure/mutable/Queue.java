package omnia.data.structure.mutable;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.Optional;
import omnia.contract.Countable;

public interface Queue<E> extends Countable, Iterable<E> {

  void enqueue(E item);

  Optional<E> dequeue();

  Optional<E> peek();

  @Override
  default Iterator<E> iterator() {
    return new Iterator<>() {

      @Override
      public boolean hasNext() {
        return Queue.this.peek().isPresent();
      }

      @Override
      public E next() {
        return dequeue().get();
      }
    };
  }

  static <E> Queue<E> masking(java.util.Queue<E> javaQueue) {
    return new Queue<>() {

      @Override
      public Optional<E> dequeue() {
        return Optional.ofNullable(javaQueue.poll());
      }

      @Override
      public Optional<E> peek() {
        return Optional.ofNullable(javaQueue.peek());
      }

      @Override
      public void enqueue(E item) {
        javaQueue.add(requireNonNull(item));
      }

      @Override
      public int count() {
        return javaQueue.size();
      }

      @Override
      public boolean isPopulated() {
        return !javaQueue.isEmpty();
      }

      @Override
      public Iterator<E> iterator() {
        return new Iterator<>() {

          @Override
          public boolean hasNext() {
            return !javaQueue.isEmpty();
          }

          @Override
          public E next() {
            return javaQueue.remove();
          }
        };
      }
    };
  }
}
