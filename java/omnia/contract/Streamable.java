package omnia.contract;

import java.util.stream.Stream;

public interface Streamable<T> {

  Stream<T> stream();
}
