package omnia.data.iterate;

import java.util.Iterator;

public final class IntegerRangeIterator implements Iterator<Integer> {
  private int next;
  private final int end;
  private final int increment;

  public static IntegerRangeIterator create(int start, int end) {
    return new IntegerRangeIterator(start, end, start <= end ? 1 : -1);
  }

  private IntegerRangeIterator(int start, int end, int increment) {
    this.next = start;
    this.end = end;
    this.increment = increment;
  }

  @Override
  public boolean hasNext() {
    return increment > 0
        ? next + increment < end
        : next + increment > end;
  }

  @Override
  public Integer next() {
    int current = next;
    next += increment;
    return current;
  }
}
