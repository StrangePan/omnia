package omnia.data.structure.tuple;

import java.util.Iterator;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.Stream;
import omnia.data.structure.immutable.ImmutableList;

class ImmutableSextuplet<T> extends ImmutableSextuple<T, T, T, T, T, T> implements Sextuplet<T> {

  ImmutableSextuplet(T first, T second, T third, T fourth, T fifth, T sixth) {
    super(first, second, third, fourth, fifth, sixth);
  }

  @Override
  public <R> Sextuplet<R> map(Function<? super T, ? extends R> mapper) {
    return Tuplet.of(mapper.apply(first()), mapper.apply(second()), mapper.apply(third()), mapper.apply(fourth()), mapper.apply(fifth()), mapper.apply(sixth()));
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
  public Septuplet<T> concat(T object) {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), object);
  }

  @Override
  public Octuplet<T> concat(Couple<T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), other.first(), other.second());
  }

  @Override
  public Nonuplet<T> concat(Triple<T, T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), other.first(), other.second(), other.third());
  }

  @Override
  public Decuplet<T> concat(Quadruple<T, T, T, T> other) {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), other.first(), other.second(), other.third(), other.fourth());
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
    return ImmutableList.of(first(), second(), third(), fourth(), fifth(), sixth());
  }
}
