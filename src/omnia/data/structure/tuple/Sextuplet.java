package omnia.data.structure.tuple;

public interface Sextuplet<T> extends Sextuple<T, T, T, T, T, T>, Tuples.AtMostSextuplet<T> {

  @Override
  Quintuplet<T> dropFirst();

  @Override
  Quintuplet<T> dropSecond();

  @Override
  Quintuplet<T> dropThird();

  @Override
  Quintuplet<T> dropFourth();

  @Override
  Quintuplet<T> dropFifth();

  @Override
  Quintuplet<T> dropSixth();

  @Override
  Septuplet<T> concatlet(T object);

  @Override
  Octuplet<T> concatlet(Couple<T, T> other);

  @Override
  Nonuplet<T> concatlet(Triple<T, T, T> other);

  @Override
  Decuplet<T> concatlet(Quadruple<T, T, T, T> other);
}
