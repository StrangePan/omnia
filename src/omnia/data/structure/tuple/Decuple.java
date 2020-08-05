package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Decuple<A, B, C, D, E, F, G, H, I, J> extends Tuples.AtLeastDecuple<A, B, C, D, E, F, G, H, I, J>, Tuples.AtMostDecuple {

  @Override
  default int count() {
    return 10;
  }

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

  @Override
  <R> Decuple<A, B, C, D, E, F, G, R, I, J> mapEighth(Function<? super H, ? extends R> mapper);

  @Override
  <R> Decuple<A, B, C, D, E, F, G, H, R, J> mapNinth(Function<? super I, ? extends R> mapper);

  @Override
  <R> Decuple<A, B, C, D, E, F, G, H, I, R> mapTenth(Function<? super J, ? extends R> mapper);

  @Override
  Nonuple<B, C, D, E, F, G, H, I, J> dropFirst();

  @Override
  Nonuple<A, C, D, E, F, G, H, I, J> dropSecond();

  @Override
  Nonuple<A, B, D, E, F, G, H, I, J> dropThird();

  @Override
  Nonuple<A, B, C, E, F, G, H, I, J> dropFourth();

  @Override
  Nonuple<A, B, C, D, F, G, H, I, J> dropFifth();

  @Override
  Nonuple<A, B, C, D, E, G, H, I, J> dropSixth();

  @Override
  Nonuple<A, B, C, D, E, F, H, I, J> dropSeventh();

  @Override
  Nonuple<A, B, C, D, E, F, G, I, J> dropEighth();

  @Override
  Nonuple<A, B, C, D, E, F, G, H, J> dropNinth();

  @Override
  Nonuple<A, B, C, D, E, F, G, H, I> dropTenth();
}
