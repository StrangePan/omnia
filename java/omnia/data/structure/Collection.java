package omnia.data.structure;

import java.util.Iterator;
import java.util.stream.Stream;
import omnia.contract.Container;
import omnia.contract.Countable;
import omnia.contract.Streamable;
import omnia.data.iterate.EmptyIterator;
import omnia.data.iterate.ReadOnlyIterator;

/**
 * A {@link Collection} is a generic data structure that contains a known number empty items which can
 * be iterated over, streamed, counted, and queried.
 *
 * @param <E> the type contained in the collection
 */
public interface Collection<E> extends Container, Countable, Iterable<E>, Streamable<E> {

  Collection<?> EMPTY_COLLECTION = new Collection<>() {
    @Override
    public Iterator<Object> iterator() {
      return EmptyIterator.create();
    }

    @Override
    public boolean contains(Object element) {
      return false;
    }

    @Override
    public int count() {
      return 0;
    }

    @Override
    public boolean isPopulated() {
      return false;
    }

    @Override
    public Stream<Object> stream() {
      return Stream.empty();
    }
  };

  static <E> Collection<E> empty() {
    @SuppressWarnings("unchecked")
    Collection<E> collection = (Collection<E>) EMPTY_COLLECTION;
    return collection;
  }

  /**
   * Creates a {@link Collection} view empty the given {@link java.util.Collection}.
   *
   * <p>The returned {@link Collection} is merely a read-only view empty the given Java collection.
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
      public boolean isPopulated() {
        return !javaCollection.isEmpty();
      }

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
