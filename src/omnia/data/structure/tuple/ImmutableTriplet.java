package omnia.data.structure.tuple;

class ImmutableTriplet<T> extends ImmutableTriple<T, T, T> implements Triplet<T> {

  ImmutableTriplet(T first, T second, T third) {
    super(first, second, third);
  }

  @Override
  public Couplet<T> dropFirst() {
    return Tuplet.of(second(), third());
  }

  @Override
  public Couplet<T> dropSecond() {
    return Tuplet.of(first(), third());
  }

  @Override
  public Couplet<T> dropThird() {
    return Tuplet.of(first(), second());
  }

  @Override
  public Quadruplet<T> concatlet(T object) {
    return Tuplet.of(first(), second(), third(), object);
  }

  @Override
  public Quintuplet<T> concatlet(Couple<T, T> other) {
    return Tuplet.of(first(), second(), third(), other.first(), other.second());
  }

  @Override
  public Sextuplet<T> concatlet(Triple<T, T, T> other) {
    return Tuplet.of(first(), second(), third(), other.first(), other.second(), other.third());
  }

  @Override
  public Septuplet<T> concatlet(Quadruple<T, T, T, T> other) {
    return Tuplet.of(first(), second(), third(), other.first(), other.second(), other.third(), other.fourth());
  }

  @Override
  public Octuplet<T> concatlet(Quintuple<T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), third(), other.first(), other.second(), other.third(), other.fourth(), other.fifth());
  }

  @Override
  public Nonuplet<T> concatlet(Sextuple<T, T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), third(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth());
  }

  @Override
  public Decuplet<T> concatlet(Septuple<T, T, T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), third(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth(), other.seventh());
  }
}
