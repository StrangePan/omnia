package omnia.data.structure.tuple;

class ImmutableSextuplet<T> extends ImmutableSextuple<T, T, T, T, T, T> implements Sextuplet<T> {

  ImmutableSextuplet(T first, T second, T third, T fourth, T fifth, T sixth) {
    super(first, second, third, fourth, fifth, sixth);
  }

  @Override
  public Quintuplet<T> dropFirst() {
    return Tuplet.of(second(), third(), fourth(), fifth(), sixth());
  }

  @Override
  public Quintuplet<T> dropSecond() {
    return Tuplet.of(first(), third(), fourth(), fifth(), sixth());
  }

  @Override
  public Quintuplet<T> dropThird() {
    return Tuplet.of(first(), second(), fourth(), fifth(), sixth());
  }

  @Override
  public Quintuplet<T> dropFourth() {
    return Tuplet.of(first(), second(), third(), fifth(), sixth());
  }

  @Override
  public Quintuplet<T> dropFifth() {
    return Tuplet.of(first(), second(), third(), fourth(), sixth());
  }

  @Override
  public Quintuplet<T> dropSixth() {
    return Tuplet.of(first(), second(), third(), fourth(), fifth());
  }

  @Override
  public Septuplet<T> concatlet(T object) {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), object);
  }

  @Override
  public Octuplet<T> concatlet(Couple<T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), other.first(), other.second());
  }

  @Override
  public Nonuplet<T> concatlet(Triple<T, T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), other.first(), other.second(), other.third());
  }

  @Override
  public Decuplet<T> concatlet(Quadruple<T, T, T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), other.first(), other.second(), other.third(), other.fourth());
  }
}
