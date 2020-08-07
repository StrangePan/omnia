package omnia.data.structure.tuple;

public interface Nonuplet<T> extends Nonuple<T, T, T, T, T, T, T, T, T>, Tuples.AtMostNonuplet<T> {

  @Override
  Octuplet<T> dropFirst();

  @Override
  Octuplet<T> dropSecond();

  @Override
  Octuplet<T> dropThird();

  @Override
  Octuplet<T> dropFourth();

  @Override
  Octuplet<T> dropFifth();

  @Override
  Octuplet<T> dropSixth();

  @Override
  Octuplet<T> dropSeventh();

  @Override
  Octuplet<T> dropEighth();

  @Override
  Octuplet<T> dropNinth();

  @Override
  Decuplet<T> concatlet(T object);
}
