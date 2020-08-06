package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Septuple<A, B, C, D, E, F, G> extends Tuples.AtLeastSeptuple<A, B, C, D, E, F, G>, Tuples.AtMostSeptuple {

  @Override
  default int count() {
    return 7;
  }

  @Override
  <R> Septuple<R, B, C, D, E, F, G> mapFirst(Function<? super A, ? extends R> mapper);

  @Override
  <R> Septuple<A, R, C, D, E, F, G> mapSecond(Function<? super B, ? extends R> mapper);

  @Override
  <R> Septuple<A, B, R, D, E, F, G> mapThird(Function<? super C, ? extends R> mapper);

  @Override
  <R> Septuple<A, B, C, R, E, F, G> mapFourth(Function<? super D, ? extends R> mapper);

  @Override
  <R> Septuple<A, B, C, D, R, F, G> mapFifth(Function<? super E, ? extends R> mapper);

  @Override
  <R> Septuple<A, B, C, D, E, R, G> mapSixth(Function<? super F, ? extends R> mapper);

  @Override
  <R> Septuple<A, B, C, D, E, F, R> mapSeventh(Function<? super G, ? extends R> mapper);

  @Override
  Sextuple<B, C, D, E, F, G> dropFirst();

  @Override
  Sextuple<A, C, D, E, F, G> dropSecond();

  @Override
  Sextuple<A, B, D, E, F, G> dropThird();

  @Override
  Sextuple<A, B, C, E, F, G> dropFourth();

  @Override
  Sextuple<A, B, C, D, F, G> dropFifth();

  @Override
  Sextuple<A, B, C, D, E, G> dropSixth();

  @Override
  Sextuple<A, B, C, D, E, F> dropSeventh();

  @Override
  <T> Octuple<T, A, B, C, D, E, F, G> prepend(T object);

  @Override
  <T> Octuple<A, B, C, D, E, F, G, T> append(T object);

  @Override
  <H> Octuple<A, B, C, D, E, F, G, H> appendAll(Monuple<H> other);

  @Override
  <H, I> Nonuple<A, B, C, D, E, F, G, H, I> appendAll(Couple<H, I> other);

  @Override
  <H, I, J> Decuple<A, B, C, D, E, F, G, H, I, J> appendAll(Triple<H, I, J> other);
}
