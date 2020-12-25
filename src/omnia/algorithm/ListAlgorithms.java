package omnia.algorithm;

import static java.util.Objects.checkFromToIndex;
import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.OptionalInt;
import omnia.data.structure.List;
import omnia.data.structure.immutable.ImmutableList;

public final class ListAlgorithms {
  private ListAlgorithms() {}

  public static <T> ImmutableList<T> sublistOf(List<T> other, int startingIndex, int endingIndex) {
    checkFromToIndex(startingIndex, endingIndex, other.count());
    ImmutableList.Builder<T> sublist = ImmutableList.builder();
    for (int i = startingIndex; i < endingIndex; i++) {
      sublist.add(other.itemAt(i));
    }
    return sublist.build();
  }

  public static <T> ImmutableList<T> reverse(List<T> other) {
    requireNonNull(other);
    ImmutableList.Builder<T> sublist = ImmutableList.builder();
    for (int i = other.count() - 1; i >= 0; i--) {
      sublist.add(other.itemAt(i));
    }
    return sublist.build();
  }

  /**
   * Converts a list to an array containing the contents of the list. Requires a seed array from
   * which to generate a new result array.
   *
   * @param list the items to copy into an array
   * @param template The seed array used to create a new array instance. Not mutated in this method.
   *     Can have any length, including 0.
   */
  public static <T> T[] toArray(List<? extends T> list, T[] template) {
    return list.stream().toArray(length -> Arrays.copyOf(template, length));
  }

  /**
   * Searches for a specific item in a sorted list of items.
   *
   * @param haystack the sorted list to search through
   * @param needle the needle to search for
   * @param comparator the {@link Comparator} to use when searching through the haystack
   * @param <T> the type of the item to search for
   * @return the index of the needle within the haystack or the empty optional if the needle was not
   *     found
   */
  public static <T> OptionalInt binarySearch(
      List<? extends T> haystack, T needle, Comparator<? super T> comparator) {
    for (int min = 0, max = haystack.count() - 1; min <= max;) {
      int mid = (min + max) / 2;
      T midValue = haystack.itemAt(mid);
      int comp = comparator.compare(midValue, needle);
      if (comp == 0) {
        return OptionalInt.of(mid);
      }
      if (comp < 0) {
        min = mid + 1;
      }
      if (comp > 0) {
        max = mid - 1;
      }
    }
    return OptionalInt.empty();
  }
}
