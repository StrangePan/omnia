package omnia.data.structure.tuple;

public interface Decuplet<T> extends Decuple<T, T, T, T, T, T, T, T, T, T>, Tuples.AtMostDecuplet<T> {

  @Override
  Nonuplet<T> dropFirst();

  @Override
  Nonuplet<T> dropSecond();

  @Override
  Nonuplet<T> dropThird();

  @Override
  Nonuplet<T> dropFourth();

  @Override
  Nonuplet<T> dropFifth();

  @Override
  Nonuplet<T> dropSixth();

  @Override
  Nonuplet<T> dropSeventh();

  @Override
  Nonuplet<T> dropEighth();

  @Override
  Nonuplet<T> dropNinth();

  @Override
  Nonuplet<T> dropTenth();
}
