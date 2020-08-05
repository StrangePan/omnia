package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Couple<A, B> extends Tuples.AtLeastCouple<A, B>, Tuples.AtMostCouple {

  @Override
  default int count() {
    return 2;
  }

  @Override
  <R> Couple<R, B> mapFirst(Function<? super A, ? extends R> mapper);

  @Override
  <R> Couple<A, R> mapSecond(Function<? super B, ? extends R> mapper);

  @Override
  Monuple<B> dropFirst();

  @Override
  Monuple<A> dropSecond();

  @Override
  <T> Triple<T, A, B> prepend(T object);

  @Override
  <T> Triple<A, B, T> append(T object);
}
