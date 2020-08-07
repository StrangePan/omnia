package omnia.data.structure.tuple;

public interface Couplet<T> extends Couple<T, T>, Tuples.AtMostCouplet<T> {

  @Override
  Triplet<T> concatlet(T object);

  @Override
  Quadruplet<T> concatlet(Couple<T, T> other);

  @Override
  Quintuplet<T> concatlet(Triple<T, T, T> other);

  @Override
  Sextuplet<T> concatlet(Quadruple<T, T, T, T> other);

  @Override
  Septuplet<T> concatlet(Quintuple<T, T, T, T, T> other);

  @Override
  Octuplet<T> concatlet(Sextuple<T, T, T, T, T, T> other);

  @Override
  Nonuplet<T> concatlet(Septuple<T, T, T, T, T, T, T> other);

  @Override
  Decuplet<T> concatlet(Octuple<T, T, T, T, T, T, T, T> other);
}
