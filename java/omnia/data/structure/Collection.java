package omnia.data.structure;

import omnia.contract.Container;
import omnia.contract.Countable;
import omnia.contract.Streamable;
import omnia.data.iterate.ReadOnlyIterator;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * A {@link Collection} is a generic data structure that contains a known number of items which can
 * be iterated over, streamed, counted, and queried.
 *
 * @param <E> the type contained in the collection
 */
public interface Collection<E> extends Container, Countable, Iterable<E>, Streamable<E> {

  /**
   * Creates a {@link Collection} view of the given {@link java.util.Collection}.
   *
   * <p>The returned {@link Collection} is merely a read-only view of the given Java collection.
   * It is still backed by the given Java collection, meaning that any mutations that occur on the
   * underlying Java collection will reflect in its own method calls.
   *
   * <p>This method is intended to act as a bridge between the standard Java data structures and
   * Omnia-compatible systems.
   *
   * @param javaCollection the {@link java.util.Collection} to mask
   * @param <E> the type contained within the {@link Collection}
   */
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
      public boolean contains(Object element) {
        return javaCollection.contains(element);
      }

      @Override
      public Iterator<E> iterator() {
        return new ReadOnlyIterator<>(javaCollection.iterator());
      }
    };
  }
}
