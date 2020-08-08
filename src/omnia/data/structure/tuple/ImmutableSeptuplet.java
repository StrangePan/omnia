package omnia.data.structure.tuple;

class ImmutableSeptuplet<T> extends ImmutableSeptuple<T, T, T, T, T, T, T> implements Septuplet<T> {

  ImmutableSeptuplet(T first, T second, T third, T fourth, T fifth, T sixth, T seventh) {
    super(first, second, third, fourth, fifth, sixth, seventh);
  }

  @Override
  public Sextuplet<T> dropFirst() {
    return Tuplet.of(second(), third(), fourth(), fifth(), sixth(), seventh());
  }

  @Override
  public Sextuplet<T> dropSecond() {
    return Tuplet.of(first(), third(), fourth(), fifth(), sixth(), seventh());
  }

  @Override
  public Sextuplet<T> dropThird() {
    return Tuplet.of(first(), second(), fourth(), fifth(), sixth(), seventh());
  }

  @Override
  public Sextuplet<T> dropFourth() {
    return Tuplet.of(first(), second(), third(), fifth(), sixth(), seventh());
  }

  @Override
  public Sextuplet<T> dropFifth() {
    return Tuplet.of(first(), second(), third(), fourth(), sixth(), seventh());
  }

  @Override
  public Sextuplet<T> dropSixth() {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), seventh());
  }

  @Override
  public Sextuplet<T> dropSeventh() {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth());
  }

  @Override
  public Octuplet<T> concat(T object) {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), object);
  }

  @Override
  public Nonuplet<T> concat(Couple<T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), other.first(), other.second());
  }

  @Override
  public Decuplet<T> concat(Triple<T, T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), other.first(), other.second(), other.third());
  }
}
