package omnia.data.structure.tuple;

public interface EmptyTuple extends Tuples.AtLeastEmptyTuple, Tuples.AtMostEmptyTuple {

  @Override
  default int count() {
    return 0;
  }

  @Override
  <T> Monuple<T> prepend(T object);

  @Override
  <T> Monuple<T> append(T object);

  @Override
  <A> Monuple<A> appendAll(Monuple<A> other);

  @Override
  <A, B> Couple<A, B> appendAll(Couple<A, B> other);

  @Override
  <A, B, C> Triple<A, B, C> appendAll(Triple<A, B, C> other);

  @Override
  <A, B, C, D> Quadruple<A, B, C, D> appendAll(Quadruple<A, B, C, D> other);

  @Override
  <A, B, C, D, E> Quintuple<A, B, C, D, E> appendAll(Quintuple<A, B, C, D, E> other);

  @Override
  <A, B, C, D, E, F> Sextuple<A, B, C, D, E, F> appendAll(Sextuple<A, B, C, D, E, F> other);

  @Override
  <A, B, C, D, E, F, G> Septuple<A, B, C, D, E, F, G> appendAll(Septuple<A, B, C, D, E, F, G> other);

  @Override
  <A, B, C, D, E, F, G, H> Octuple<A, B, C, D, E, F, G, H> appendAll(Octuple<A, B, C, D, E, F, G, H> other);

  @Override
  <A, B, C, D, E, F, G, H, I> Nonuple<A, B, C, D, E, F, G, H, I> appendAll(Nonuple<A, B, C, D, E, F, G, H, I> other);

  @Override
  <A, B, C, D, E, F, G, H, I, J> Decuple<A, B, C, D, E, F, G, H, I, J> appendAll(Decuple<A, B, C, D, E, F, G, H, I, J> other);
}
