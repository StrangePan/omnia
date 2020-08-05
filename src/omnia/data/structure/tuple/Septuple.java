package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Septuple<A, B, C, D, E, F, G> extends Tuples.AtLeastSeptuple<A, B, C, D, E, F, G> {

  @Override
  <R> Septuple<R, B, C, D, E, F, G> mapFirst(Function<? super A, ? extends R> mapper);

  @Override
  <R> Septuple<A, R, C, D, E, F, G> mapSecond(Function<? super B, ? extends R> mapper);

  @Override
  <R> Septuple<A, B, R, D, E, F, G> mapThird(Function<? super C, ? extends R> mapper);
}
