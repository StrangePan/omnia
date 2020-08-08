package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Couplet<T> extends Couple<T, T>, Tuples.AtMostCouplet<T> {

  static <T> Couplet<T> of(T first, T second) {
    return new ImmutableCouplet<>(first, second);
  }

  @Override
  <R> Couplet<R> map(Function<? super T, ? extends R> mapper);

  @Override
  Triplet<T> concat(T object);

  @Override
  Quadruplet<T> concat(Couple<T, T> other);

  @Override
  Quintuplet<T> concat(Triple<T, T, T> other);

  @Override
  Sextuplet<T> concat(Quadruple<T, T, T, T> other);

  @Override
  Septuplet<T> concat(Quintuple<T, T, T, T, T> other);

  @Override
  Octuplet<T> concat(Sextuple<T, T, T, T, T, T> other);

  @Override
  Nonuplet<T> concat(Septuple<T, T, T, T, T, T, T> other);

  @Override
  Decuplet<T> concat(Octuple<T, T, T, T, T, T, T, T> other);
}
