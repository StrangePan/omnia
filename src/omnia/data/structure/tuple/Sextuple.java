package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Sextuple<A, B, C, D, E, F> extends Tuples.AtLeastSextuple<A, B, C, D, E, F>, Tuples.AtMostSextuple {

  @Override
  default int count() {
    return 6;
  }

  @Override
  <R> Sextuple<R, B, C, D, E, F> mapFirst(Function<? super A, ? extends R> mapper);

  @Override
  <R> Sextuple<A, R, C, D, E, F> mapSecond(Function<? super B, ? extends R> mapper);

  @Override
  <R> Sextuple<A, B, R, D, E, F> mapThird(Function<? super C, ? extends R> mapper);

  @Override
  <R> Sextuple<A, B, C, R, E, F> mapFourth(Function<? super D, ? extends R> mapper);

  @Override
  <R> Sextuple<A, B, C, D, R, F> mapFifth(Function<? super E, ? extends R> mapper);

  @Override
  <R> Sextuple<A, B, C, D, E, R> mapSixth(Function<? super F, ? extends R> mapper);

  @Override
  Quintuple<B, C, D, E, F> dropFirst();

  @Override
  Quintuple<A, C, D, E, F> dropSecond();

  @Override
  Quintuple<A, B, D, E, F> dropThird();

  @Override
  Quintuple<A, B, C, E, F> dropFourth();

  @Override
  Quintuple<A, B, C, D, F> dropFifth();

  @Override
  Quintuple<A, B, C, D, E> dropSixth();

  @Override
  <T> Septuple<T, A, B, C, D, E, F> prepend(T object);

  @Override
  <T> Septuple<A, B, C, D, E, F, T> append(T object);

  @Override
  <G> Septuple<A, B, C, D, E, F, G> appendAll(Monuple<G> other);

  @Override
  <G, H> Octuple<A, B, C, D, E, F, G, H> appendAll(Couple<G, H> other);

  @Override
  <G, H, I> Nonuple<A, B, C, D, E, F, G, H, I> appendAll(Triple<G, H, I> other);

  @Override
  <G, H, I, J> Decuple<A, B, C, D, E, F, G, H, I, J> appendAll(Quadruple<G, H, I, J> other);
}
