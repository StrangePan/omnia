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
  public Quintuplet<T> concat(T object) {
    return Tuplet.of(first(), second(), third(), fourth(), object);
  }

  @Override
  public Sextuplet<T> concat(Couple<T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), other.first(), other.second());
  }

  @Override
  public Septuplet<T> concat(Triple<T, T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), other.first(), other.second(), other.third());
  }

  @Override
  public Octuplet<T> concat(Quadruple<T, T, T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), other.first(), other.second(), other.third(), other.fourth());
  }

  @Override
  public Nonuplet<T> concat(Quintuple<T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), other.first(), other.second(), other.third(), other.fourth(), other.fifth());
  }

  @Override
  public Decuplet<T> concat(Sextuple<T, T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth());
  }
}
