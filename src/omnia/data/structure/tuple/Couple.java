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
  <T> Triple<A, B, T> append(T object);

  @Override
  <C, D> Quadruple<A, B, C, D> append(Couple<C, D> other);

  @Override
  <C, D, E> Quintuple<A, B, C, D, E> append(Triple<C, D, E> other);

  @Override
  <C, D, E, F> Sextuple<A, B, C, D, E, F> append(Quadruple<C, D, E, F> other);

  @Override
  <C, D, E, F, G> Septuple<A, B, C, D, E, F, G> append(Quintuple<C, D, E, F, G> other);

  @Override
  <C, D, E, F, G, H> Octuple<A, B, C, D, E, F, G, H> append(Sextuple<C, D, E, F, G, H> other);

  @Override
  <C, D, E, F, G, H, I> Nonuple<A, B, C, D, E, F, G, H, I> append(Septuple<C, D, E, F, G, H, I> other);

  @Override
  <C, D, E, F, G, H, I, J> Decuple<A, B, C, D, E, F, G, H, I, J> append(Octuple<C, D, E, F, G, H, I, J> other);
}