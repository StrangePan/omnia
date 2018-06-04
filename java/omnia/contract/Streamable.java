package omnia.contract;

import java.util.stream.Stream;

/**
 * A {@link Streamable} is an object whose contents can be streamed to and is compatible with
 * Java's {@link Stream} facilities.
 *
 * @param <T> the type that can be streamed
 */
public interface Streamable<T> {

  /** Creates and returns a stream of this object's contents. */
  Stream<T> stream();
}
