package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Monuple<A> extends Tuples.AtLeastMonuple<A>, Tuples.AtMostMonuple {

  @Override
  default int count() {
    return 1;
  }

  @Override
  <R> Monuple<R> mapFirst(Function<? super A, ? extends R> mapper);

  @Override
  Tuple dropFirst();

  @Override
  <T> Couple<T, A> prepend(T object);

  @Override
  <T> Couple<A, T> append(T object);
}
