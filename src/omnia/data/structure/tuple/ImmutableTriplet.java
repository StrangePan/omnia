package omnia.data.structure.tuple;

import java.util.Iterator;
import java.util.OptionalInt;
import java.util.stream.Stream;
import omnia.data.structure.immutable.ImmutableList;

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
  public Quadruplet<T> concat(T object) {
    return Tuplet.of(first(), second(), third(), object);
  }

  @Override
  public Quintuplet<T> concat(Couple<T, T> other) {
    return Tuplet.of(first(), second(), third(), other.first(), other.second());
  }

  @Override
  public Sextuplet<T> concat(Triple<T, T, T> other) {
    return Tuplet.of(first(), second(), third(), other.first(), other.second(), other.third());
  }

  @Override
  public Septuplet<T> concat(Quadruple<T, T, T, T> other) {
    return Tuplet.of(first(), second(), third(), other.first(), other.second(), other.third(), other.fourth());
  }

  @Override
  public Octuplet<T> concat(Quintuple<T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), third(), other.first(), other.second(), other.third(), other.fourth(), other.fifth());
  }

  @Override
  public Nonuplet<T> concat(Sextuple<T, T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), third(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth());
  }

  @Override
  public Decuplet<T> concat(Septuple<T, T, T, T, T, T, T> other) {
    return Tuplet.of(first(), second(), third(), other.first(), other.second(), other.third(), other.fourth(), other.fifth(), other.sixth(), other.seventh());
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
    return ImmutableList.of(first(), second(), third());
  }

}
