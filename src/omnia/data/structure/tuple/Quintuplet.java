package omnia.data.structure.tuple;

public interface Quintuplet<T> extends Quintuple<T, T, T, T, T>, Tuples.AtMostQuintuplet<T> {

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
