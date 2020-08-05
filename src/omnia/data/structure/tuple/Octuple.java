package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Octuple<A, B, C, D, E, F, G, H> extends Septuple<A, B, C, D, E, F, G> {

  H eighth();

  @Override
  <R> Octuple<R, B, C, D, E, F, G, H> mapFirst(Function<? super A, ? extends R> mapper);
}
