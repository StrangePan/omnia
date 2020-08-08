package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Septuplet<T> extends Septuple<T, T, T, T, T, T, T>, Tuples.AtMostSeptuplet<T> {

  static <T> Septuplet<T> of(T first, T second, T third, T fourth, T fifth, T sixth, T seventh) {
    return new ImmutableSeptuplet<>(first, second, third, fourth, fifth, sixth, seventh);
  }

  @Override
  <R> Septuplet<R> map(Function<? super T, ? extends R> mapper);

  @Override
  Sextuplet<T> dropFirst();

  @Override
  Sextuplet<T> dropSecond();

  @Override
  Sextuplet<T> dropThird();

  @Override
  Sextuplet<T> dropFourth();

  @Override
  Sextuplet<T> dropFifth();

  @Override
  Sextuplet<T> dropSixth();

  @Override
  Sextuplet<T> dropSeventh();

  @Override
  Octuplet<T> concat(T object);

  @Override
  Nonuplet<T> concat(Couple<T, T> other);

  @Override
  Decuplet<T> concat(Triple<T, T, T> other);
}
