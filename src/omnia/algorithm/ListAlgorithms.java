package omnia.algorithm;

import static java.util.Objects.checkFromToIndex;
import static java.util.Objects.requireNonNull;

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
}
