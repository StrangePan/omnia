package omnia.data.structure.tuple;

class ImmutableCouplet<T> extends ImmutableCouple<T, T> implements Couplet<T> {

  ImmutableCouplet(T first, T second) {
    super(first, second);
  }

  @Override
  public Triplet<T> concat(T object) {
    return Tuplet.of(first(), second(), object);
  }

  @Override
  public Quadruplet<T> concat(Couple<T, T> other) {
    return Tuplet.of(first(), second(), other.first(), other.second());
  }

  @Override
  public Quintuplet<T> concat(Triple<T, T, T> other) {
    return Tuplet.of(first(), second(), other.first(), other.second(), other.third());
  }

  @Override
  public Sextuplet<T> concat(Quadruple<T, T, T, T> other) {
    return Tuplet.of(first(), second(), other.first(), other.second(), other.third(), other.fourth());
  }

  @Override
  public Septuplet<T> concat(Quintuple<T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), other.first(), other.second(), other.third(), other.fourth(), other.fifth());
  }

  @Override
  public Octuplet<T> concat(Sextuple<T, T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth());
  }

  @Override
  public Nonuplet<T> concat(Septuple<T, T, T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth(), other.seventh());
  }

  @Override
  public Decuplet<T> concat(Octuple<T, T, T, T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth(), other.seventh(), other.eighth());
  }
}
