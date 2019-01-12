package omnia.data.structure;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Stream;
import omnia.data.iterate.ReadOnlyIterator;

public interface Set<E> extends Collection<E> {

  static <E> Set<E> masking(java.util.Set<E> javaSet) {
    return new Set<>() {

      @Override
      public boolean isPopulated() {
        return !javaSet.isEmpty();
      }

      @Override
      public int count() {
        return javaSet.size();
      }

      @Override
      public boolean contains(Object element) {
        return javaSet.contains(element);
      }

      @Override
      public Iterator<E> iterator() {
        return new ReadOnlyIterator<>(javaSet.iterator());
      }

      @Override
      public Stream<E> stream() {
        return javaSet.stream();
      }
    };
  }

  static <E> Set<E> masking(java.util.Collection<E> javaCollection) {
    return Set.masking(new HashSet<>(javaCollection));
  }

  Set<?> EMPTY_SET = masking(Collections.emptySet());

  @SuppressWarnings("unchecked")
  static <E> Set<E> empty() {
    return (Set<E>) EMPTY_SET;
  }
}
