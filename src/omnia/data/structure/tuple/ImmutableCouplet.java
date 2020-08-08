package omnia.data.structure.tuple;

import java.util.Iterator;
import java.util.OptionalInt;
import java.util.stream.Stream;
import omnia.data.structure.immutable.ImmutableList;

class ImmutableCouplet<T> extends ImmutableCouple<T, T> implements Couplet<T> {

  ImmutableCouplet(T first, T second) {
    super(first, second);
  }

  @Override
  public Triplet<T> concat(T object) {
    return Tuplet.of(first(), second(), object);
  }

  @Override
  public Quadruplet<T> concat(Couple<T, T> other) {
    return Tuplet.of(first(), second(), other.first(), other.second());
  }

  @Override
  public Quintuplet<T> concat(Triple<T, T, T> other) {
    return Tuplet.of(first(), second(), other.first(), other.second(), other.third());
  }

  @Override
  public Sextuplet<T> concat(Quadruple<T, T, T, T> other) {
    return Tuplet.of(first(), second(), other.first(), other.second(), other.third(), other.fourth());
  }

  @Override
  public Septuplet<T> concat(Quintuple<T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), other.first(), other.second(), other.third(), other.fourth(), other.fifth());
  }

  @Override
  public Octuplet<T> concat(Sextuple<T, T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth());
  }

  @Override
  public Nonuplet<T> concat(Septuple<T, T, T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth(), other.seventh());
  }

  @Override
  public Decuplet<T> concat(Octuple<T, T, T, T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth(), other.seventh(), other.eighth());
  }

  @Override
  public Iterator<T> iterator() {
    return toActualList().iterator();
  }

  @Override
  public T itemAt(int index) {
    return toActualList().itemAt(index);
  }

  @Override
  public OptionalInt indexOf(Object item) {
    return toActualList().indexOf(item);
  }

  @Override
  public Stream<T> stream() {
    return toActualList().stream();
  }

  @Override
  public boolean containsUnknownTyped(Object element) {
    return toActualList().containsUnknownTyped(element);
  }

  private ImmutableList<T> toActualList() {
    return ImmutableList.of(first(), second());
  }
}
