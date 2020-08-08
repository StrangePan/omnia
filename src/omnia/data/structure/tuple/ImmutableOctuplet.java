package omnia.data.structure.tuple;

class ImmutableOctuplet<T> extends ImmutableOctuple<T, T, T, T, T, T, T, T> implements Octuplet<T> {

  ImmutableOctuplet(T first, T second, T third, T fourth, T fifth, T sixth, T seventh, T eighth) {
    super(first, second, third, fourth, fifth, sixth, seventh, eighth);
  }

  @Override
  public Septuplet<T> dropFirst() {
    return Tuplet.of(second(), third(), fourth(), fifth(), sixth(), seventh(), eighth());
  }

  @Override
  public Septuplet<T> dropSecond() {
    return Tuplet.of(first(), third(), fourth(), fifth(), sixth(), seventh(), eighth());
  }

  @Override
  public Septuplet<T> dropThird() {
    return Tuplet.of(first(), second(), fourth(), fifth(), sixth(), seventh(), eighth());
  }

  @Override
  public Septuplet<T> dropFourth() {
    return Tuplet.of(first(), second(), third(), fifth(), sixth(), seventh(), eighth());
  }

  @Override
  public Septuplet<T> dropFifth() {
    return Tuplet.of(first(), second(), third(), fourth(), sixth(), seventh(), eighth());
  }

  @Override
  public Septuplet<T> dropSixth() {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), seventh(), eighth());
  }

  @Override
  public Septuplet<T> dropSeventh() {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), eighth());
  }

  @Override
  public Septuplet<T> dropEighth() {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh());
  }

  @Override
  public Nonuplet<T> concat(T object) {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), object);
  }

  @Override
  public Decuplet<T> concat(Couple<T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), other.first(), other.second());
  }
}
