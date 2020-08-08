package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Quintuple<A, B, C, D, E> extends Tuples.AtLeastQuintuple<A, B, C, D, E>, Tuples.AtMostQuintuple {

  @Override
  default int count() {
    return 5;
  }

  @Override
  <R> Quintuple<R, B, C, D, E> mapFirst(Function<? super A, ? extends R> mapper);

  @Override
  <R> Quintuple<A, R, C, D, E> mapSecond(Function<? super B, ? extends R> mapper);

  @Override
  <R> Quintuple<A, B, R, D, E> mapThird(Function<? super C, ? extends R> mapper);

  @Override
  <R> Quintuple<A, B, C, R, E> mapFourth(Function<? super D, ? extends R> mapper);

  @Override
  <R> Quintuple<A, B, C, D, R> mapFifth(Function<? super E, ? extends R> mapper);

  @Override
  Quadruple<B, C, D, E> dropFirst();

  @Override
  Quadruple<A, C, D, E> dropSecond();

  @Override
  Quadruple<A, B, D, E> dropThird();

  @Override
  Quadruple<A, B, C, E> dropFourth();

  @Override
  Quadruple<A, B, C, D> dropFifth();

  @Override
  <T> Sextuple<A, B, C, D, E, T> append(T object);

  @Override
  <F, G> Septuple<A, B, C, D, E, F, G> append(Couple<F, G> other);

  @Override
  <F, G, H> Octuple<A, B, C, D, E, F, G, H> append(Triple<F, G, H> other);

  @Override
  <F, G, H, I> Nonuple<A, B, C, D, E, F, G, H, I> append(Quadruple<F, G, H, I> other);

  @Override
  <F, G, H, I, J> Decuple<A, B, C, D, E, F, G, H, I, J> append(Quintuple<F, G, H, I, J> other);
}
