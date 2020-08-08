package omnia.data.structure.tuple;

class ImmutableQuintuplet<T> extends ImmutableQuintuple<T, T, T, T, T> implements Quintuplet<T> {

  ImmutableQuintuplet(T first, T second, T third, T fourth, T fifth) {
    super(first, second, third, fourth, fifth);
  }

  @Override
  public Quadruplet<T> dropFirst() {
    return Tuplet.of(second(), third(), fourth(), fifth());
  }

  @Override
  public Quadruplet<T> dropSecond() {
    return Tuplet.of(first(), third(), fourth(), fifth());
  }

  @Override
  public Quadruplet<T> dropThird() {
    return Tuplet.of(first(), second(), fourth(), fifth());
  }

  @Override
  public Quadruplet<T> dropFourth() {
    return Tuplet.of(first(), second(), third(), fifth());
  }

  @Override
  public Quadruplet<T> dropFifth() {
    return Tuplet.of(first(), second(), third(), fourth());
  }

  @Override
  public Sextuplet<T> concat(T object) {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), object);
  }

  @Override
  public Septuplet<T> concat(Couple<T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), other.first(), other.second());
  }

  @Override
  public Octuplet<T> concat(Triple<T, T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), other.first(), other.second(), other.third());
  }

  @Override
  public Nonuplet<T> concat(Quadruple<T, T, T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), other.first(), other.second(), other.third(), other.fourth());
  }

  @Override
  public Decuplet<T> concat(Quintuple<T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), other.first(), other.second(), other.third(), other.fourth(), other.fifth());
  }
}
