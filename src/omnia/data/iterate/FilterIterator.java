package omnia.data.iterate;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class FilterIterator<E> implements Iterator<E> {
  private final Iterator<E> source;
  private final Predicate<? super E> filter;
  private boolean didPeekNext;
  private E peekedNext;
  private boolean hasNext;

  public FilterIterator(Iterator<E> source, Predicate<? super E> filter) {
    this.source = requireNonNull(source);
    this.filter = requireNonNull(filter);
  }

  @Override
  public boolean hasNext() {
    maybePeekNext();
    return hasNext;
  }

  @Override
  public E next() {
    maybePeekNext();
    if (!hasNext) {
      throw new NoSuchElementException("invoked next() when there is no next");
    }
    return clearNext();
  }

  private void maybePeekNext() {
    if (didPeekNext) {
      return;
    }
    didPeekNext = true;
    hasNext = false;
    while (source.hasNext()) {
      peekedNext = source.next();
      if (filter.test(peekedNext)) {
        hasNext = true;
        break;
      }
    }
  }

  private E clearNext() {
    didPeekNext = false;
    E next = peekedNext;
    peekedNext = null;
    return next;
  }

  @Override
  public void remove() {
    if (didPeekNext) {
      throw new IllegalStateException(
          "Cannot invoke remove() after calling hasNext(). This is a limitation of "
          + "FilterIterator.");
    }
    source.remove();
  }
}
