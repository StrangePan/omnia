package omnia.data.structure.tuple;

class ImmutableDecuplet<T> extends ImmutableDecuple<T, T, T, T, T, T, T, T, T, T> implements Decuplet<T> {

  ImmutableDecuplet(T first, T second, T third, T fourth, T fifth, T sixth, T seventh, T eighth, T ninth, T tenth) {
    super(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth);
  }

  @Override
  public Nonuplet<T> dropFirst() {
    return Tuplet.of(second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), tenth());
  }

  @Override
  public Nonuplet<T> dropSecond() {
    return Tuplet.of(first(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), tenth());
  }

  @Override
  public Nonuplet<T> dropThird() {
    return Tuplet.of(first(), second(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), tenth());
  }

  @Override
  public Nonuplet<T> dropFourth() {
    return Tuplet.of(first(), second(), third(), fifth(), sixth(), seventh(), eighth(), ninth(), tenth());
  }

  @Override
  public Nonuplet<T> dropFifth() {
    return Tuplet.of(first(), second(), third(), fourth(), sixth(), seventh(), eighth(), ninth(), tenth());
  }

  @Override
  public Nonuplet<T> dropSixth() {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), seventh(), eighth(), ninth(), tenth());
  }

  @Override
  public Nonuplet<T> dropSeventh() {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), eighth(), ninth(), tenth());
  }

  @Override
  public Nonuplet<T> dropEighth() {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), ninth(), tenth());
  }

  @Override
  public Nonuplet<T> dropNinth() {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), tenth());
  }

  @Override
  public Nonuplet<T> dropTenth() {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth());
  }
}
