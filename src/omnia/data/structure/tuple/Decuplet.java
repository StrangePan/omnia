package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Decuplet<T> extends Decuple<T, T, T, T, T, T, T, T, T, T>, Tuples.AtMostDecuplet<T> {

  static <T> Decuplet<T> of(T first, T second, T third, T fourth, T fifth, T sixth, T seventh, T eighth, T ninth, T tenth) {
    return new ImmutableDecuplet<>(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth);
  }

  @Override
  <R> Decuplet<R> map(Function<? super T, ? extends R> mapper);

  @Override
  Nonuplet<T> dropFirst();

  @Override
  Nonuplet<T> dropSecond();

  @Override
  Nonuplet<T> dropThird();

  @Override
  Nonuplet<T> dropFourth();

  @Override
  Nonuplet<T> dropFifth();

  @Override
  Nonuplet<T> dropSixth();

  @Override
  Nonuplet<T> dropSeventh();

  @Override
  Nonuplet<T> dropEighth();

  @Override
  Nonuplet<T> dropNinth();

  @Override
  Nonuplet<T> dropTenth();
}
