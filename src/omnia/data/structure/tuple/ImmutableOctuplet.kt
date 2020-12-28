package omnia.data.structure.tuple

import omnia.data.structure.immutable.ImmutableList
import java.util.OptionalInt
import java.util.function.Function
import java.util.stream.Stream

internal class ImmutableOctuplet<T>(first: T, second: T, third: T, fourth: T, fifth: T, sixth: T, seventh: T, eighth: T) : ImmutableOctuple<T, T, T, T, T, T, T, T>(first, second, third, fourth, fifth, sixth, seventh, eighth), Octuplet<T> {
  override fun <R> map(mapper: Function<in T, out R>): Octuplet<R> {
    return Tuplet.of(mapper.apply(first()), mapper.apply(second()), mapper.apply(third()), mapper.apply(fourth()), mapper.apply(fifth()), mapper.apply(sixth()), mapper.apply(seventh()), mapper.apply(eighth()))
  }

  override fun dropFirst(): Septuplet<T> {
    return Tuplet.of(second(), third(), fourth(), fifth(), sixth(), seventh(), eighth())
  }

  override fun dropSecond(): Septuplet<T> {
    return Tuplet.of(first(), third(), fourth(), fifth(), sixth(), seventh(), eighth())
  }

  override fun dropThird(): Septuplet<T> {
    return Tuplet.of(first(), second(), fourth(), fifth(), sixth(), seventh(), eighth())
  }

  override fun dropFourth(): Septuplet<T> {
    return Tuplet.of(first(), second(), third(), fifth(), sixth(), seventh(), eighth())
  }

  override fun dropFifth(): Septuplet<T> {
    return Tuplet.of(first(), second(), third(), fourth(), sixth(), seventh(), eighth())
  }

  override fun dropSixth(): Septuplet<T> {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), seventh(), eighth())
  }

  override fun dropSeventh(): Septuplet<T> {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), eighth())
  }

  override fun dropEighth(): Septuplet<T> {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh())
  }

  override fun concat(`object`: T): Nonuplet<T> {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), `object`)
  }

  override fun concat(other: Couple<T, T>): Decuplet<T> {
    return Tuplet.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth(), other.first(), other.second())
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
    return ImmutableList.of(first(), second(), third(), fourth(), fifth(), sixth(), seventh(), eighth())
  }
}