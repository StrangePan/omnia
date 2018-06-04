package omnia.data.structure;

import java.util.Iterator;
import java.util.OptionalInt;
import java.util.stream.Stream;

public interface List<E> extends Collection<E> {

  E getAt(int index);

  OptionalInt indexOf(E element);

  static <E> List<E> masking(java.util.List<E> javaList) {
    return new List<>() {

      @Override
      public Stream<E> stream() {
        return javaList.stream();
      }

      @Override
      public int count() {
        return javaList.size();
      }

      @Override
      public boolean contains(E element) {
        return javaList.contains(element);
      }

      @Override
      public Iterator<E> iterator() {
        return new ReadOnlyIterator<>(javaList.iterator());
      }

      @Override
      public E getAt(int index) {
        return javaList.get(index);
      }

      @Override
      public OptionalInt indexOf(E element) {
        int index = javaList.indexOf(element);
        return index < 0 ? OptionalInt.empty() : OptionalInt.of(index);
      }
    };
  }
}
