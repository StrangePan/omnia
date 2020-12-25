package omnia.data.structure;

import java.util.Iterator;
import java.util.Objects;
import omnia.contract.Countable;
import omnia.data.iterate.IntegerRangeIterator;
import omnia.data.structure.immutable.ImmutableList;

/** A simple class representing a range of integers. */
public final class IntRange implements Countable, Iterable<Integer> {
  private final int start;
  private final int length;

  public static IntRange just(int point) {
    return startingAt(point).withLength(1);
  }

  public static Builder startingAt(int start) {
    return new Builder() {

      @Override
      public IntRange endingAt(int end) {
        return new IntRange(start, end - start);
      }

      @Override
      public IntRange endingAtInclusive(int inclusiveEnd) {
        return new IntRange(start, inclusiveEnd + 1 - start);
      }

      @Override
      public IntRange withLength(int length) {
        return new IntRange(start, length);
      }
    };
  }

  public interface Builder {
    IntRange endingAt(int end);

    IntRange endingAtInclusive(int inclusiveEnd);

    IntRange withLength(int length);
  }

  private IntRange(int start, int length) {
    if (length < 0) {
      throw new IllegalArgumentException(
          String.format(
              "range start must come at or before the end. start=%d end=%d length=%d",
              start,
              start + length,
              length));
    }
    this.start = start;
    this.length = length;
  }

  /**
   * The <b>inclusive</b> starting index of the range. Guaranteed to represent a valid index into
   * a list.
   */
  public int start() {
    return start;
  }

  /**
   * The <b>non-inclusive</b> ending index of the range. Not guaranteed to represent a valid
   * index into a list.
   */
  public int end() {
    return start + length;
  }

  /**
   * The <b>inclusive</b> end index of the range. Guaranteed to represent a valid index into a
   * list.
   */
  public int endInclusive() {
    return end() - 1;
  }

  /**
   * The number of indices contained within the range. Equivalent to
   * {@code {@link #end()} - {@link #start()}}.
   */
  @Override
  public int count() {
    return length;
  }

  public boolean contains(int n) {
    return start <= n && n < start + length;
  }

  @Override
  public boolean isPopulated() {
    return length > 0;
  }

  @Override
  public Iterator<Integer> iterator() {
    return IntegerRangeIterator.create(start, end());
  }

  private <E> ImmutableList<E> asSublistOf(ImmutableList<E> list) {
    return list.sublistStartingAt(start()).to(end());
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, length);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof IntRange
        && ((IntRange) obj).start == start
        && ((IntRange) obj).length == length;
  }

  @Override
  public String toString() {
    return String.format("IntRange[%d–%d)", start(), endInclusive());
  }
}
