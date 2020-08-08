package omnia.data.structure.tuple;

import java.util.function.Function;

public interface Octuplet<T> extends Octuple<T, T, T, T, T, T, T, T>, Tuples.AtMostOctuplet<T> {

  static <T> Octuplet<T> of(T first, T second, T third, T fourth, T fifth, T sixth, T seventh, T eighth) {
    return new ImmutableOctuplet<>(first, second, third, fourth, fifth, sixth, seventh, eighth);
  }

  @Override
  <R> Octuplet<R> map(Function<? super T, ? extends R> mapper);

  @Override
  Septuplet<T> dropFirst();

  @Override
  Septuplet<T> dropSecond();

  @Override
  Septuplet<T> dropThird();

  @Override
  Septuplet<T> dropFourth();

  @Override
  Septuplet<T> dropFifth();

  @Override
  Septuplet<T> dropSixth();

  @Override
  Septuplet<T> dropSeventh();

  @Override
  Septuplet<T> dropEighth();

  @Override
  Nonuplet<T> concat(T object);

  @Override
  Decuplet<T> concat(Couple<T, T> other);
}
