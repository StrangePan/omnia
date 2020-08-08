package omnia.data.structure.tuple;

import java.util.Iterator;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.Stream;
import omnia.data.structure.immutable.ImmutableList;

class ImmutableSeptuplet<T> extends ImmutableSeptuple<T, T, T, T, T, T, T> implements Septuplet<T> {

  ImmutableSeptuplet(T first, T second, T third, T fourth, T fifth, T sixth, T seventh) {
    super(first, second, third, fourth, fifth, sixth, seventh);
  }

  @Override
  public <R> Septuplet<R> map(Function<? super T, ? extends R> mapper) {
    return Tuplet.of(mapper.apply(first()), mapper.apply(second()), mapper.apply(third()), mapper.apply(fourth()), mapper.apply(fifth()), mapper.apply(sixth()), mapper.apply(seventh()));
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
    return ImmutableList.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh());
  }
}
