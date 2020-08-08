package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Sextuplet<T> extends Sextuple<T, T, T, T, T, T>, Tuples.AtMostSextuplet<T> {

  static <T> Sextuplet<T> of(T first, T second, T third, T fourth, T fifth, T sixth) {
    return new ImmutableSextuplet<>(first, second, third, fourth, fifth, sixth);
  }

  @Override
  <R> Sextuplet<R> map(Function<? super T, ? extends R> mapper);

  @Override
  Quintuplet<T> dropFirst();

  @Override
  Quintuplet<T> dropSecond();

  @Override
  Quintuplet<T> dropThird();

  @Override
  Quintuplet<T> dropFourth();

  @Override
  Quintuplet<T> dropFifth();

  @Override
  Quintuplet<T> dropSixth();

  @Override
  Septuplet<T> concat(T object);

  @Override
  Octuplet<T> concat(Couple<T, T> other);

  @Override
  Nonuplet<T> concat(Triple<T, T, T> other);

  @Override
  Decuplet<T> concat(Quadruple<T, T, T, T> other);
}
