package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Quadruple<A, B, C, D> extends Tuples.AtLeastQuadruple<A, B, C, D> {

  @Override
  <R> Quadruple<R, B, C, D> mapFirst(Function<? super A, ? extends R> mapper);

  @Override
  <R> Quadruple<A, R, C, D> mapSecond(Function<? super B, ? extends R> mapper);

  @Override
  <R> Quadruple<A, B, R, D> mapThird(Function<? super C, ? extends R> mapper);
}
