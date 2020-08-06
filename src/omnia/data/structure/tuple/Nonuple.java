package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Nonuple<A, B, C, D, E, F, G, H, I> extends Tuples.AtLeastNonuple<A, B, C, D, E, F, G, H, I>, Tuples.AtMostNonuple {

  @Override
  default int count() {
    return 9;
  }

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

  @Override
  <R> Nonuple<A, B, C, D, E, F, G, H, R> mapNinth(Function<? super I, ? extends R> mapper);

  @Override
  Octuple<B, C, D, E, F, G, H, I> dropFirst();

  @Override
  Octuple<A, C, D, E, F, G, H, I> dropSecond();

  @Override
  Octuple<A, B, D, E, F, G, H, I> dropThird();

  @Override
  Octuple<A, B, C, E, F, G, H, I> dropFourth();

  @Override
  Octuple<A, B, C, D, F, G, H, I> dropFifth();

  @Override
  Octuple<A, B, C, D, E, G, H, I> dropSixth();

  @Override
  Octuple<A, B, C, D, E, F, H, I> dropSeventh();

  @Override
  Octuple<A, B, C, D, E, F, G, I> dropEighth();

  @Override
  Octuple<A, B, C, D, E, F, G, H> dropNinth();

  @Override
  <T> Decuple<A, B, C, D, E, F, G, H, I, T> append(T object);

  @Override
  <J> Decuple<A, B, C, D, E, F, G, H, I, J> concat(Monuple<J> other);
}
