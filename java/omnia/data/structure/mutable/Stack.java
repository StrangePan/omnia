package omnia.data.structure.mutable;

import omnia.contract.Countable;

import java.util.Iterator;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public interface Stack<E> extends Countable, Iterable<E> {

  void push(E item);

  Optional<E> pop();

  static <E> Stack<E> masking(java.util.Stack<E> javaStack) {
    return new Stack<>() {

      @Override
      public void push(E item) {
        javaStack.push(requireNonNull(item));
      }

      @Override
      public Optional<E> pop() {
        return javaStack.isEmpty() ? Optional.empty() : Optional.ofNullable(javaStack.pop());
      }

      @Override
      public int count() {
        return javaStack.size();
      }

      @Override
      public boolean isPopulated() {
        return !javaStack.isEmpty();
      }

      @Override
      public Iterator<E> iterator() {
        return new Iterator<>() {

          @Override
          public boolean hasNext() {
            return !javaStack.empty();
          }

          @Override
          public E next() {
            return javaStack.pop();
          }
        };
      }
    };
  }
}
