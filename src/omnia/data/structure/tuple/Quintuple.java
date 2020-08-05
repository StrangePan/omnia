package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Quintuple<A, B, C, D, E> extends Tuples.AtLeastQuintuple<A, B, C, D, E> {

  @Override
  <R> Quintuple<R, B, C, D, E> mapFirst(Function<? super A, ? extends R> mapper);
}
