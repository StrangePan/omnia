package omnia.data.iterate;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/** An {@link Iterator} that takes a source iterator and maps its elements to a new value. */
public final class MappingIterator<T, R> implements Iterator<R> {

  private final Iterator<? extends T> source;
  private final Function<? super T, ? extends R> mapper;

  public MappingIterator(Iterator<? extends T> source, Function<? super T, ? extends R> mapper) {
    this.source = requireNonNull(source);
    this.mapper = requireNonNull(mapper);
  }

  @Override
  public boolean hasNext() {
    return source.hasNext();
  }

  @Override
  public R next() {
    return mapper.apply(source.next());
  }

  @Override
  public void remove() {
    source.remove();
  }

  @Override
  public void forEachRemaining(Consumer<? super R> action) {
    source.forEachRemaining(x -> action.accept(mapper.apply(x)));
  }
}
