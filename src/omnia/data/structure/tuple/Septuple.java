package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Septuple<A, B, C, D, E, F, G> extends Tuples.AtLeastSeptuple<A, B, C, D, E, F, G> {

  @Override
  <R> Septuple<R, B, C, D, E, F, G> mapFirst(Function<? super A, ? extends R> mapper);
}
