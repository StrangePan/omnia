package omnia.data.structure;

import omnia.contract.Container;
import omnia.contract.Countable;
import omnia.contract.Streamable;

import java.util.Iterator;
import java.util.stream.Stream;

public interface Collection<E> extends Container<E>, Countable, Iterable<E>, Streamable<E> {

  static <E> Collection<E> masking(java.util.Collection<E> javaCollection) {
    return new Collection<>() {

      @Override
      public Stream<E> stream() {
        return javaCollection.stream();
      }

      @Override
      public int count() {
        return javaCollection.size();
      }

      @Override
      public boolean contains(E element) {
        return javaCollection.contains(element);
      }

      @Override
      public Iterator<E> iterator() {
        return new ReadOnlyIterator<>(javaCollection.iterator());
      }
    };
  }
}
