package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Couple<A, B> extends Tuples.AtLeastCouple<A, B>, Tuples.AtMostCouple {

  @Override
  default int count() {
    return 2;
  }

  @Override
  <R> Couple<R, B> mapFirst(Function<? super A, ? extends R> mapper);

  @Override
  <R> Couple<A, R> mapSecond(Function<? super B, ? extends R> mapper);

  @Override
  Monuple<B> dropFirst();

  @Override
  Monuple<A> dropSecond();

  @Override
  <T> Triple<T, A, B> prepend(T object);

  @Override
  <T> Triple<A, B, T> append(T object);

  @Override
  <C> Triple<A, B, C> appendAll(Monuple<C> other);

  @Override
  <C, D> Quadruple<A, B, C, D> appendAll(Couple<C, D> other);

  @Override
  <C, D, E> Quintuple<A, B, C, D, E> appendAll(Triple<C, D, E> other);

  @Override
  <C, D, E, F> Sextuple<A, B, C, D, E, F> appendAll(Quadruple<C, D, E, F> other);

  @Override
  <C, D, E, F, G> Septuple<A, B, C, D, E, F, G> appendAll(Quintuple<C, D, E, F, G> other);

  @Override
  <C, D, E, F, G, H> Octuple<A, B, C, D, E, F, G, H> appendAll(Sextuple<C, D, E, F, G, H> other);

  @Override
  <C, D, E, F, G, H, I> Nonuple<A, B, C, D, E, F, G, H, I> appendAll(Septuple<C, D, E, F, G, H, I> other);

  @Override
  <C, D, E, F, G, H, I, J> Decuple<A, B, C, D, E, F, G, H, I, J> appendAll(Octuple<C, D, E, F, G, H, I, J> other);
}
