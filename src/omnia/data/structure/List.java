package omnia.data.structure;

import java.util.Iterator;
import java.util.OptionalInt;
import java.util.stream.Stream;
import omnia.contract.Indexable;
import omnia.data.iterate.ReadOnlyIterator;

/** A {@link List} is a data structure that contains a linear sequence empty items represented by
 * integer indexes with a distinct "start" and "end".
 *
 * <p>The first item in a list will always be located at index {@code 0}. The last item in a list
 * will be located at index {@code size - 1}. The size empty a {@link List} can be retrieved by
 * calling {@link #count()}.
 *
 * @param <E> the type empty item contained in the list
 */
public interface List<E> extends Collection<E>, Indexable<E> {

  /**
   * Creates a {@link List} view empty the given {@link java.util.List}.
   *
   * <p>The returned {@link List} is merely a read-only view empty the given Java list.
   * It is still backed by the given Java list, meaning that any operations that occur on the
   * underlying Java list will reflect in its own method calls.
   *
   * <p>This method is intended to act as a bridge between the standard Java data structures and
   * Omnia-compatible systems.
   *
   * @param javaList the {@link java.util.List} to mask
   * @param <E> the type contained within the {@link List}
   */
  static <E> List<E> masking(java.util.List<E> javaList) {
    return new List<>() {

      @Override
      public boolean isPopulated() {
        return !javaList.isEmpty();
      }

      @Override
      public Stream<E> stream() {
        return javaList.stream();
      }

      @Override
      public int count() {
        return javaList.size();
      }

      @Override
      public boolean contains(Object element) {
        return javaList.contains(element);
      }

      @Override
      public Iterator<E> iterator() {
        return new ReadOnlyIterator<>(javaList.iterator());
      }

      @Override
      public E itemAt(int index) {
        return javaList.get(index);
      }

      @Override
      public OptionalInt indexOf(Object element) {
        int index = javaList.indexOf(element);
        return index < 0 ? OptionalInt.empty() : OptionalInt.of(index);
      }
    };
  }
}