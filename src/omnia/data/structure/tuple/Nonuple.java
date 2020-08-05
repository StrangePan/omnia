package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Nonuple<A, B, C, D, E, F, G, H, I> extends Tuples.AtLeastNonuple<A, B, C, D, E, F, G, H, I> {

  @Override
  <R> Nonuple<R, B, C, D, E, F, G, H, I> mapFirst(Function<? super A, ? extends R> mapper);

  @Override
  <R> Nonuple<A, R, C, D, E, F, G, H, I> mapSecond(Function<? super B, ? extends R> mapper);

  @Override
  <R> Nonuple<A, B, R, D, E, F, G, H, I> mapThird(Function<? super C, ? extends R> mapper);

  @Override
  <R> Nonuple<A, B, C, R, E, F, G, H, I> mapFourth(Function<? super D, ? extends R> mapper);

  @Override
  <R> Nonuple<A, B, C, D, R, F, G, H, I> mapFifth(Function<? super E, ? extends R> mapper);

  @Override
  <R> Nonuple<A, B, C, D, E, R, G, H, I> mapSixth(Function<? super F, ? extends R> mapper);

  @Override
  <R> Nonuple<A, B, C, D, E, F, R, H, I> mapSeventh(Function<? super G, ? extends R> mapper);

  @Override
  <R> Nonuple<A, B, C, D, E, F, G, R, I> mapEighth(Function<? super H, ? extends R> mapper);
}
