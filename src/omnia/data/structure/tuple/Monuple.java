package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Monuple<A> extends Tuples.AtLeastMonuple<A>, Tuples.AtMostMonuple {

  @Override
  default int count() {
    return 1;
  }

  @Override
  <R> Monuple<R> mapFirst(Function<? super A, ? extends R> mapper);

  @Override
  Tuple dropFirst();

  @Override
  <T> Couple<T, A> prepend(T object);

  @Override
  <T> Couple<A, T> append(T object);

  @Override
  <B> Couple<A, B> appendAll(Monuple<B> other);

  @Override
  <B, C> Triple<A, B, C> appendAll(Couple<B, C> other);

  @Override
  <B, C, D> Quadruple<A, B, C, D> appendAll(Triple<B, C, D> other);

  @Override
  <B, C, D, E> Quintuple<A, B, C, D, E> appendAll(Quadruple<B, C, D, E> other);

  @Override
  <B, C, D, E, F> Sextuple<A, B, C, D, E, F> appendAll(Quintuple<B, C, D, E, F> other);

  @Override
  <B, C, D, E, F, G> Septuple<A, B, C, D, E, F, G> appendAll(Sextuple<B, C, D, E, F, G> other);

  @Override
  <B, C, D, E, F, G, H> Octuple<A, B, C, D, E, F, G, H> appendAll(Septuple<B, C, D, E, F, G, H> other);

  @Override
  <B, C, D, E, F, G, H, I> Nonuple<A, B, C, D, E, F, G, H, I> appendAll(Octuple<B, C, D, E, F, G, H, I> other);

  @Override
  <B, C, D, E, F, G, H, I, J> Decuple<A, B, C, D, E, F, G, H, I, J> appendAll(Nonuple<B, C, D, E, F, G, H, I, J> other);
}
