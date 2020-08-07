package omnia.data.structure.tuple;

class ImmutableCouplet<T> extends ImmutableCouple<T, T> implements Couplet<T> {

  ImmutableCouplet(T first, T second) {
    super(first, second);
  }

  @Override
  public Triplet<T> concatlet(T object) {
    return Tuplet.of(first(), second(), object);
  }

  @Override
  public Quadruplet<T> concatlet(Couple<T, T> other) {
    return Tuplet.of(first(), second(), other.first(), other.second());
  }

  @Override
  public Quintuplet<T> concatlet(Triple<T, T, T> other) {
    return Tuplet.of(first(), second(), other.first(), other.second(), other.third());
  }

  @Override
  public Sextuplet<T> concatlet(Quadruple<T, T, T, T> other) {
    return Tuplet.of(first(), second(), other.first(), other.second(), other.third(), other.fourth());
  }

  @Override
  public Septuplet<T> concatlet(Quintuple<T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), other.first(), other.second(), other.third(), other.fourth(), other.fifth());
  }

  @Override
  public Octuplet<T> concatlet(Sextuple<T, T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth());
  }

  @Override
  public Nonuplet<T> concatlet(Septuple<T, T, T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth(), other.seventh());
  }

  @Override
  public Decuplet<T> concatlet(Octuple<T, T, T, T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth(), other.seventh(), other.eighth());
  }
}
