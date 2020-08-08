package omnia.data.structure.tuple;

import java.util.Iterator;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.Stream;
import omnia.data.structure.immutable.ImmutableList;

class ImmutableDecuplet<T> extends ImmutableDecuple<T, T, T, T, T, T, T, T, T, T> implements Decuplet<T> {

  ImmutableDecuplet(T first, T second, T third, T fourth, T fifth, T sixth, T seventh, T eighth, T ninth, T tenth) {
    super(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth);
  }

  @Override
  public <R> Decuplet<R> map(Function<? super T, ? extends R> mapper) {
    return Tuplet.of(mapper.apply(first()), mapper.apply(second()), mapper.apply(third()), mapper.apply(fourth()), mapper.apply(fifth()), mapper.apply(sixth()), mapper.apply(seventh()), mapper.apply(eighth()), mapper.apply(ninth()), mapper.apply(tenth()));
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
    return ImmutableList.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), ninth(), tenth());
  }
}