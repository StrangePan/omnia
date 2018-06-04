package omnia.data.structure.mutable;

import omnia.data.structure.Set;

import java.util.Iterator;
import java.util.stream.Stream;

public interface MutableSet<E> extends Set<E>, MutableCollection<E> {

  static <E> MutableSet<E> masking(java.util.Set<E> javaSet) {
    return new MutableSet<>() {
      @Override
      public void add(E element) {
        javaSet.add(element);
      }

      @Override
      public boolean remove(E element) {
        return javaSet.remove(element);
      }

      @Override
      public void clear() {
        javaSet.clear();
      }

      @Override
      public Iterator<E> iterator() {
        return javaSet.iterator();
      }

      @Override
      public boolean contains(E element) {
        return javaSet.contains(element);
      }

      @Override
      public int count() {
        return javaSet.size();
      }

      @Override
      public Stream<E> stream() {
        return javaSet.stream();
      }
    };
  }
}
