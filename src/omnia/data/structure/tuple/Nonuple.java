package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Nonuple<A, B, C, D, E, F, G, H, I> extends Tuples.AtLeastNonuple<A, B, C, D, E, F, G, H, I> {

  @Override
  <R> Nonuple<R, B, C, D, E, F, G, H, I> mapFirst(Function<? super A, ? extends R> mapper);
}
