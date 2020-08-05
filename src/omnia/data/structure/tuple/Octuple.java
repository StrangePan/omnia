package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Octuple<A, B, C, D, E, F, G, H> extends Tuples.AtLeastOctuple<A, B, C, D, E, F, G, H> {

  @Override
  <R> Octuple<R, B, C, D, E, F, G, H> mapFirst(Function<? super A, ? extends R> mapper);
}
