package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Quadruple<A, B, C, D> extends Tuples.AtLeastQuadruple<A, B, C, D>, Tuples.AtMostQuadruple {

  @Override
  default int count() {
    return 4;
  }

  @Override
  <R> Quadruple<R, B, C, D> mapFirst(Function<? super A, ? extends R> mapper);

  @Override
  <R> Quadruple<A, R, C, D> mapSecond(Function<? super B, ? extends R> mapper);

  @Override
  <R> Quadruple<A, B, R, D> mapThird(Function<? super C, ? extends R> mapper);

  @Override
  <R> Quadruple<A, B, C, R> mapFourth(Function<? super D, ? extends R> mapper);

  @Override
  Triple<B, C, D> dropFirst();

  @Override
  Triple<A, C, D> dropSecond();

  @Override
  Triple<A, B, D> dropThird();

  @Override
  Triple<A, B, C> dropFourth();

  @Override
  <T> Quintuple<A, B, C, D, T> append(T object);

  @Override
  <E, F> Sextuple<A, B, C, D, E, F> append(Couple<E, F> other);

  @Override
  <E, F, G> Septuple<A, B, C, D, E, F, G> append(Triple<E, F, G> other);

  @Override
  <E, F, G, H> Octuple<A, B, C, D, E, F, G, H> append(Quadruple<E, F, G, H> other);

  @Override
  <E, F, G, H, I> Nonuple<A, B, C, D, E, F, G, H, I> append(Quintuple<E, F, G, H, I> other);

  @Override
  <E, F, G, H, I, J> Decuple<A, B, C, D, E, F, G, H, I, J> append(Sextuple<E, F, G, H, I, J> other);
}
