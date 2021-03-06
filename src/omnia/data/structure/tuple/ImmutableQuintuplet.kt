package omnia.data.structure.tuple

import java.util.OptionalInt
import java.util.function.Function
import java.util.stream.Stream
import omnia.data.structure.immutable.ImmutableList

internal class ImmutableQuintuplet<T>(first: T, second: T, third: T, fourth: T, fifth: T) :
  ImmutableQuintuple<T, T, T, T, T>(first, second, third, fourth, fifth), Quintuplet<T> {

  override fun <R> map(mapper: Function<in T, out R>): Quintuplet<R> {
    return Tuplet.of(
      mapper.apply(first()),
      mapper.apply(second()),
      mapper.apply(third()),
      mapper.apply(fourth()),
      mapper.apply(fifth())
    )
  }

  override fun dropFirst(): Quadruplet<T> {
    return Tuplet.of(second(), third(), fourth(), fifth())
  }

  override fun dropSecond(): Quadruplet<T> {
    return Tuplet.of(first(), third(), fourth(), fifth())
  }

  override fun dropThird(): Quadruplet<T> {
    return Tuplet.of(first(), second(), fourth(), fifth())
  }

  override fun dropFourth(): Quadruplet<T> {
    return Tuplet.of(first(), second(), third(), fifth())
  }

  override fun dropFifth(): Quadruplet<T> {
    return Tuplet.of(first(), second(), third(), fourth())
  }

  override fun concat(`object`: T): Sextuplet<T> {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), `object`)
  }

  override fun concat(other: Couple<T, T>): Septuplet<T> {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), other.first(), other.second())
  }

  override fun concat(other: Triple<T, T, T>): Octuplet<T> {
    return Tuplet.of(
      first(),
      second(),
      third(),
      fourth(),
      fifth(),
      other.first(),
      other.second(),
      other.third()
    )
  }

  override fun concat(other: Quadruple<T, T, T, T>): Nonuplet<T> {
    return Tuplet.of(
      first(),
      second(),
      third(),
      fourth(),
      fifth(),
      other.first(),
      other.second(),
      other.third(),
      other.fourth()
    )
  }

  override fun concat(other: Quintuple<T, T, T, T, T>): Decuplet<T> {
    return Tuplet.of(
      first(),
      second(),
      third(),
      fourth(),
      fifth(),
      other.first(),
      other.second(),
      other.third(),
      other.fourth(),
      other.fifth()
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
    return ImmutableList.of(first(), second(), third(), fourth(), fifth())
  }
}