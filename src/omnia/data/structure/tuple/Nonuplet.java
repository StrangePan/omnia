package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Nonuplet<T> extends Nonuple<T, T, T, T, T, T, T, T, T>, Tuples.AtMostNonuplet<T> {

  static <T> Nonuplet<T> of(T first, T second, T third, T fourth, T fifth, T sixth, T seventh, T eighth, T ninth) {
    return new ImmutableNonuplet<>(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth);
  }

  @Override
  <R> Nonuplet<R> map(Function<? super T, ? extends R> mapper);

  @Override
  Octuplet<T> dropFirst();

  @Override
  Octuplet<T> dropSecond();

  @Override
  Octuplet<T> dropThird();

  @Override
  Octuplet<T> dropFourth();

  @Override
  Octuplet<T> dropFifth();

  @Override
  Octuplet<T> dropSixth();

  @Override
  Octuplet<T> dropSeventh();

  @Override
  Octuplet<T> dropEighth();

  @Override
  Octuplet<T> dropNinth();

  @Override
  Decuplet<T> concat(T object);
}
