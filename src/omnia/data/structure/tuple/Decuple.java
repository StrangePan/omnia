package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Decuple<A, B, C, D, E, F, G, H, I, J> extends Tuples.AtLeastDecuple<A, B, C, D, E, F, G, H, I, J> {

  @Override
  <R> Decuple<R, B, C, D, E, F, G, H, I, J> mapFirst(Function<? super A, ? extends R> mapper);

  @Override
  <R> Decuple<A, R, C, D, E, F, G, H, I, J> mapSecond(Function<? super B, ? extends R> mapper);

  @Override
  <R> Decuple<A, B, R, D, E, F, G, H, I, J> mapThird(Function<? super C, ? extends R> mapper);

  @Override
  <R> Decuple<A, B, C, R, E, F, G, H, I, J> mapFourth(Function<? super D, ? extends R> mapper);

  @Override
  <R> Decuple<A, B, C, D, R, F, G, H, I, J> mapFifth(Function<? super E, ? extends R> mapper);

  @Override
  <R> Decuple<A, B, C, D, E, R, G, H, I, J> mapSixth(Function<? super F, ? extends R> mapper);

  @Override
  <R> Decuple<A, B, C, D, E, F, R, H, I, J> mapSeventh(Function<? super G, ? extends R> mapper);
}
