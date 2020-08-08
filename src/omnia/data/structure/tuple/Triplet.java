package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Triplet<T> extends Triple<T, T, T>, Tuples.AtMostTriplet<T> {

  static <T> Triplet<T> of(T first, T second, T third) {
    return new ImmutableTriplet<>(first, second, third);
  }

  @Override
  <R> Triplet<R> map(Function<? super T, ? extends R> mapper);

  @Override
  Couplet<T> dropFirst();

  @Override
  Couplet<T> dropSecond();

  @Override
  Couplet<T> dropThird();

  @Override
  Quadruplet<T> concat(T object);

  @Override
  Quintuplet<T> concat(Couple<T, T> other);

  @Override
  Sextuplet<T> concat(Triple<T, T, T> other);

  @Override
  Septuplet<T> concat(Quadruple<T, T, T, T> other);

  @Override
  Octuplet<T> concat(Quintuple<T, T, T, T, T> other);

  @Override
  Nonuplet<T> concat(Sextuple<T, T, T, T, T, T> other);

  @Override
  Decuplet<T> concat(Septuple<T, T, T, T, T, T, T> other);
}
