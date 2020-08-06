package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Triple<A, B, C> extends Tuples.AtLeastTriple<A, B, C>, Tuples.AtMostTriple {

  @Override
  default int count() {
    return 3;
  }

  @Override
  <R> Triple<R, B, C> mapFirst(Function<? super A, ? extends R> mapper);

  @Override
  <R> Triple<A, R, C> mapSecond(Function<? super B, ? extends R> mapper);

  @Override
  <R> Triple<A, B, R> mapThird(Function<? super C, ? extends R> mapper);

  @Override
  Couple<B, C> dropFirst();

  @Override
  Couple<A, C> dropSecond();

  @Override
  Couple<A, B> dropThird();

  @Override
  <T> Quadruple<A, B, C, T> append(T object);

  @Override
  <D> Quadruple<A, B, C, D> concat(Monuple<D> other);

  @Override
  <D, E> Quintuple<A, B, C, D, E> concat(Couple<D, E> other);

  @Override
  <D, E, F> Sextuple<A, B, C, D, E, F> concat(Triple<D, E, F> other);

  @Override
  <D, E, F, G> Septuple<A, B, C, D, E, F, G> concat(Quadruple<D, E, F, G> other);

  @Override
  <D, E, F, G, H> Octuple<A, B, C, D, E, F, G, H> concat(Quintuple<D, E, F, G, H> other);

  @Override
  <D, E, F, G, H, I> Nonuple<A, B, C, D, E, F, G, H, I> concat(Sextuple<D, E, F, G, H, I> other);

  @Override
  <D, E, F, G, H, I, J> Decuple<A, B, C, D, E, F, G, H, I, J> concat(Septuple<D, E, F, G, H, I, J> other);
}
