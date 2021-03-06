package omnia.data.structure.tuple

import java.util.OptionalInt
import java.util.function.Function
import java.util.stream.Stream
import omnia.data.structure.immutable.ImmutableList

internal class ImmutableSextuplet<T>(first: T, second: T, third: T, fourth: T, fifth: T, sixth: T) :
  ImmutableSextuple<T, T, T, T, T, T>(first, second, third, fourth, fifth, sixth), Sextuplet<T> {

  override fun <R> map(mapper: Function<in T, out R>): Sextuplet<R> {
    return Tuplet.of(
      mapper.apply(first()),
      mapper.apply(second()),
      mapper.apply(third()),
      mapper.apply(fourth()),
      mapper.apply(fifth()),
      mapper.apply(sixth())
    )
  }

  override fun dropFirst(): Quintuplet<T> {
    return Tuplet.of(second(), third(), fourth(), fifth(), sixth())
  }

  override fun dropSecond(): Quintuplet<T> {
    return Tuplet.of(first(), third(), fourth(), fifth(), sixth())
  }

  override fun dropThird(): Quintuplet<T> {
    return Tuplet.of(first(), second(), fourth(), fifth(), sixth())
  }

  override fun dropFourth(): Quintuplet<T> {
    return Tuplet.of(first(), second(), third(), fifth(), sixth())
  }

  override fun dropFifth(): Quintuplet<T> {
    return Tuplet.of(first(), second(), third(), fourth(), sixth())
  }

  override fun dropSixth(): Quintuplet<T> {
    return Tuplet.of(first(), second(), third(), fourth(), fifth())
  }

  override fun concat(`object`: T): Septuplet<T> {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), `object`)
  }

  override fun concat(other: Couple<T, T>): Octuplet<T> {
    return Tuplet.of(
      first(),
      second(),
      third(),
      fourth(),
      fifth(),
      sixth(),
      other.first(),
      other.second()
    )
  }

  override fun concat(other: Triple<T, T, T>): Nonuplet<T> {
    return Tuplet.of(
      first(),
      second(),
      third(),
      fourth(),
      fifth(),
      sixth(),
      other.first(),
      other.second(),
      other.third()
    )
  }

  override fun concat(other: Quadruple<T, T, T, T>): Decuplet<T> {
    return Tuplet.of(
      first(),
      second(),
      third(),
      fourth(),
      fifth(),
      sixth(),
      other.first(),
      other.second(),
      other.third(),
      other.fourth()
    )
  }

  override fun iterator(): Iterator<T> {
    return toActualList().iterator()
  }

  override fun itemAt(index: Int): T {
    return toActualList().itemAt(index)
  }

  override fun indexOf(item: Any?): OptionalInt {
    return toActualList().indexOf(item)
  }

  override fun stream(): Stream<T> {
    return toActualList().stream()
  }

  override fun containsUnknownTyped(item: Any?): Boolean {
    return toActualList().containsUnknownTyped(item)
  }

  private fun toActualList(): ImmutableList<T> {
    return ImmutableList.of(first(), second(), third(), fourth(), fifth(), sixth())
  }
}