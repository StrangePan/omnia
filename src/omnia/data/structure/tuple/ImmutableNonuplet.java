package omnia.data.structure.tuple;

import java.util.Iterator;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.Stream;
import omnia.data.structure.immutable.ImmutableList;

class ImmutableNonuplet<T> extends ImmutableNonuple<T, T, T, T, T, T, T, T, T> implements Nonuplet<T> {

  ImmutableNonuplet(T first, T second, T third, T fourth, T fifth, T sixth, T seventh, T eighth, T ninth) {
    super(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth);
  }

  @Override
  public <R> Nonuplet<R> map(Function<? super T, ? extends R> mapper) {
    return Tuplet.of(mapper.apply(first()), mapper.apply(second()), mapper.apply(third()), mapper.apply(fourth()), mapper.apply(fifth()), mapper.apply(sixth()), mapper.apply(seventh()), mapper.apply(eighth()), mapper.apply(ninth()));
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
    return ImmutableList.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth());
  }
}
