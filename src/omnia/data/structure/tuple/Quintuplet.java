package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Quintuplet<T> extends Quintuple<T, T, T, T, T>, Tuples.AtMostQuintuplet<T> {

  static <T> Quintuplet<T> of(T first, T second, T third, T fourth, T fifth) {
    return new ImmutableQuintuplet<>(first, second, third, fourth, fifth);
  }

  @Override
  <R> Quintuplet<R> map(Function<? super T, ? extends R> mapper);

  @Override
  Quadruplet<T> dropFirst();

  @Override
  Quadruplet<T> dropSecond();

  @Override
  Quadruplet<T> dropThird();

  @Override
  Quadruplet<T> dropFourth();

  @Override
  Quadruplet<T> dropFifth();

  @Override
  Sextuplet<T> concat(T object);

  @Override
  Septuplet<T> concat(Couple<T, T> other);

  @Override
  Octuplet<T> concat(Triple<T, T, T> other);

  @Override
  Nonuplet<T> concat(Quadruple<T, T, T, T> other);

  @Override
  Decuplet<T> concat(Quintuple<T, T, T, T, T> other);
}
