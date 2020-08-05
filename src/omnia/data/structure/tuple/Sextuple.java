package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Sextuple<A, B, C, D, E, F> extends Quintuple<A, B, C, D, E> {

  F sixth();

  @Override
  <R> Sextuple<R, B, C, D, E, F> mapFirst(Function<? super A, ? extends R> mapper);
}
