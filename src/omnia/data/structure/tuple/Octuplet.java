package omnia.data.structure.tuple;

public interface Octuplet<T> extends Octuple<T, T, T, T, T, T, T, T>, Tuples.AtMostOctuplet<T> {

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
  Nonuplet<T> concatlet(T object);

  @Override
  Decuplet<T> concatlet(Couple<T, T> other);
}
