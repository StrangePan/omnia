package omnia.algorithm;

import static java.util.Objects.checkFromToIndex;
import static java.util.Objects.requireNonNull;

import java.util.Arrays;
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
}
