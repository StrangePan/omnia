package omnia.data.structure.tuple;

public interface Quadruplet<T> extends Quadruple<T, T, T, T>, Tuples.AtMostQuadruplet<T> {

  @Override
  Triplet<T> dropFirst();

  @Override
  Triplet<T> dropSecond();

  @Override
  Triplet<T> dropThird();

  @Override
  Triplet<T> dropFourth();

  @Override
  Quintuplet<T> concat(T object);

  @Override
  Sextuplet<T> concat(Couple<T, T> other);

  @Override
  Septuplet<T> concat(Triple<T, T, T> other);

  @Override
  Octuplet<T> concat(Quadruple<T, T, T, T> other);

  @Override
  Nonuplet<T> concat(Quintuple<T, T, T, T, T> other);

  @Override
  Decuplet<T> concat(Sextuple<T, T, T, T, T, T> other);
}
