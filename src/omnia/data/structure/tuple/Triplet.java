package omnia.data.structure.tuple;

public interface Triplet<T> extends Triple<T, T, T>, Tuples.AtMostTriplet<T> {

  @Override
  Couplet<T> dropFirst();

  @Override
  Couplet<T> dropSecond();

  @Override
  Couplet<T> dropThird();

  @Override
  Quadruplet<T> concatlet(T object);

  @Override
  Quintuplet<T> concatlet(Couple<T, T> other);

  @Override
  Sextuplet<T> concatlet(Triple<T, T, T> other);

  @Override
  Septuplet<T> concatlet(Quadruple<T, T, T, T> other);

  @Override
  Octuplet<T> concatlet(Quintuple<T, T, T, T, T> other);

  @Override
  Nonuplet<T> concatlet(Sextuple<T, T, T, T, T, T> other);

  @Override
  Decuplet<T> concatlet(Septuple<T, T, T, T, T, T, T> other);
}
