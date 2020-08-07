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
  Sextuplet<T> concatlet(T object);

  @Override
  Septuplet<T> concatlet(Couple<T, T> other);

  @Override
  Octuplet<T> concatlet(Triple<T, T, T> other);

  @Override
  Nonuplet<T> concatlet(Quadruple<T, T, T, T> other);

  @Override
  Decuplet<T> concatlet(Quintuple<T, T, T, T, T> other);
}
