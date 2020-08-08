package omnia.data.structure.tuple;

public interface Septuplet<T> extends Septuple<T, T, T, T, T, T, T>, Tuples.AtMostSeptuplet<T> {

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
