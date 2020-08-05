package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Sextuple<A, B, C, D, E, F> extends Tuples.AtLeastSextuple<A, B, C, D, E, F> {

  @Override
  <R> Sextuple<R, B, C, D, E, F> mapFirst(Function<? super A, ? extends R> mapper);

  @Override
  <R> Sextuple<A, R, C, D, E, F> mapSecond(Function<? super B, ? extends R> mapper);
}
