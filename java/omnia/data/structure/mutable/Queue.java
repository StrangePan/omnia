package omnia.data.structure.mutable;

import omnia.contract.Countable;

import java.util.Iterator;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public interface Queue<E> extends Countable, Iterable<E> {

  Optional<E> nextAndRemove();

  Optional<E> next();

  void enqueue(E item);

  @Override
  default Iterator<E> iterator() {
    return new Iterator<>() {

      @Override
      public boolean hasNext() {
        return Queue.this.next().isPresent();
      }

      @Override
      public E next() {
        return nextAndRemove().get();
      }
    };
  }

  static <E> Queue<E> masking(java.util.Queue<E> javaQueue) {
    return new Queue<>() {

      @Override
      public Optional<E> nextAndRemove() {
        return Optional.ofNullable(javaQueue.poll());
      }

      @Override
      public Optional<E> next() {
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
