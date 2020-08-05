package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Decuple<A, B, C, D, E, F, G, H, I, J> extends Tuples.AtLeastDecuple<A, B, C, D, E, F, G, H, I, J> {

  @Override
  <R> Decuple<R, B, C, D, E, F, G, H, I, J> mapFirst(Function<? super A, ? extends R> mapper);
}
