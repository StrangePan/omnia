package omnia.data.structure.tuple;

class ImmutableNonuplet<T> extends ImmutableNonuple<T, T, T, T, T, T, T, T, T> implements Nonuplet<T> {

  ImmutableNonuplet(T first, T second, T third, T fourth, T fifth, T sixth, T seventh, T eighth, T ninth) {
    super(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth);
  }

  @Override
  public Octuplet<T> dropFirst() {
    return Tuplet.of(second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth());
  }

  @Override
  public Octuplet<T> dropSecond() {
    return Tuplet.of(first(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth());
  }

  @Override
  public Octuplet<T> dropThird() {
    return Tuplet.of(first(), second(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth());
  }

  @Override
  public Octuplet<T> dropFourth() {
    return Tuplet.of(first(), second(), third(), fifth(), sixth(), seventh(), eighth(), ninth());
  }

  @Override
  public Octuplet<T> dropFifth() {
    return Tuplet.of(first(), second(), third(), fourth(), sixth(), seventh(), eighth(), ninth());
  }

  @Override
  public Octuplet<T> dropSixth() {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), seventh(), eighth(), ninth());
  }

  @Override
  public Octuplet<T> dropSeventh() {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), eighth(), ninth());
  }

  @Override
  public Octuplet<T> dropEighth() {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), ninth());
  }

  @Override
  public Octuplet<T> dropNinth() {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth());
  }

  @Override
  public Decuplet<T> concat(T object) {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), object);
  }
}
