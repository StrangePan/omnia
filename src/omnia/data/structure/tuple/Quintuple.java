package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Quintuple<A, B, C, D, E> extends Tuples.AtLeastQuintuple<A, B, C, D, E> {

  @Override
  <R> Quintuple<R, B, C, D, E> mapFirst(Function<? super A, ? extends R> mapper);

  @Override
  <R> Quintuple<A, R, C, D, E> mapSecond(Function<? super B, ? extends R> mapper);

  @Override
  <R> Quintuple<A, B, R, D, E> mapThird(Function<? super C, ? extends R> mapper);

  @Override
  <R> Quintuple<A, B, C, R, E> mapFourth(Function<? super D, ? extends R> mapper);
}
