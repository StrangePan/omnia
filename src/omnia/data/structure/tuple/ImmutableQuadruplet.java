package omnia.data.structure.tuple;

class ImmutableQuadruplet<T> extends ImmutableQuadruple<T, T, T, T> implements Quadruplet<T> {

  ImmutableQuadruplet(T first, T second, T third, T fourth) {
    super(first, second, third, fourth);
  }

  @Override
  public Triplet<T> dropFirst() {
    return Tuplet.of(second(), third(), fourth());
  }

  @Override
  public Triplet<T> dropSecond() {
    return Tuplet.of(first(), third(), fourth());
  }

  @Override
  public Triplet<T> dropThird() {
    return Tuplet.of(first(), second(), fourth());
  }

  @Override
  public Triplet<T> dropFourth() {
    return Tuplet.of(first(), second(), third());
  }

  @Override
  public Quintuplet<T> concatlet(T object) {
    return Tuplet.of(first(), second(), third(), fourth(), object);
  }

  @Override
  public Sextuplet<T> concatlet(Couple<T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), other.first(), other.second());
  }

  @Override
  public Septuplet<T> concatlet(Triple<T, T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), other.first(), other.second(), other.third());
  }

  @Override
  public Octuplet<T> concatlet(Quadruple<T, T, T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), other.first(), other.second(), other.third(), other.fourth());
  }

  @Override
  public Nonuplet<T> concatlet(Quintuple<T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), other.first(), other.second(), other.third(), other.fourth(), other.fifth());
  }

  @Override
  public Decuplet<T> concatlet(Sextuple<T, T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth());
  }
}
