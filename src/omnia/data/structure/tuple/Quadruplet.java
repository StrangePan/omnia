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
  Quintuplet<T> concatlet(T object);

  @Override
  Sextuplet<T> concatlet(Couple<T, T> other);

  @Override
  Septuplet<T> concatlet(Triple<T, T, T> other);

  @Override
  Octuplet<T> concatlet(Quadruple<T, T, T, T> other);

  @Override
  Nonuplet<T> concatlet(Quintuple<T, T, T, T, T> other);

  @Override
  Decuplet<T> concatlet(Sextuple<T, T, T, T, T, T> other);
}
