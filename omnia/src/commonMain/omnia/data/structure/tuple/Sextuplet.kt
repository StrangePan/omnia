package omnia.data.structure.tuple

import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.tuple.Tuples.AtMostSextuplet

class Sextuplet<T : Any>(first: T, second: T, third: T, fourth: T, fifth: T, sixth: T) :
  Sextuple<T, T, T, T, T, T>(first, second, third, fourth, fifth, sixth), AtMostSextuplet<T> {

  override fun <R : Any> map(mapper: (T) -> R): Sextuplet<R> {
    return Tuplet.of(
      mapper(first),
      mapper(second),
      mapper(third),
      mapper(fourth),
      mapper(fifth),
      mapper(sixth)
    )
  }

  override fun dropFirst(): Quintuplet<T> {
    return Tuplet.of(second, third, fourth, fifth, sixth)
  }

  override fun dropSecond(): Quintuplet<T> {
    return Tuplet.of(first, third, fourth, fifth, sixth)
  }

  override fun dropThird(): Quintuplet<T> {
    return Tuplet.of(first, second, fourth, fifth, sixth)
  }

  override fun dropFourth(): Quintuplet<T> {
    return Tuplet.of(first, second, third, fifth, sixth)
  }

  override fun dropFifth(): Quintuplet<T> {
    return Tuplet.of(first, second, third, fourth, sixth)
  }

  override fun dropSixth(): Quintuplet<T> {
    return Tuplet.of(first, second, third, fourth, fifth)
  }

  override fun concat(item: T): Septuplet<T> {
    return Tuplet.of(first, second, third, fourth, fifth, sixth, item)
  }

  override fun concat(other: Couple<T, T>): Octuplet<T> {
    return Tuplet.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      other.first,
      other.second
    )
  }

  override fun concat(other: Triple<T, T, T>): Nonuplet<T> {
    return Tuplet.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      other.first,
      other.second,
      other.third
    )
  }

  override fun concat(other: Quadruple<T, T, T, T>): Decuplet<T> {
    return Tuplet.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      other.first,
      other.second,
      other.third,
      other.fourth
    )
  }

  override fun iterator(): Iterator<T> {
    return toActualList().iterator()
  }

  override fun itemAt(index: Int): T {
    return toActualList().itemAt(index)
  }

  override fun indexOf(item: Any?): Int? {
    return toActualList().indexOf(item)
  }

  override fun containsUnknownTyped(item: Any?): Boolean {
    return toActualList().containsUnknownTyped(item)
  }

  private fun toActualList(): ImmutableList<T> {
    return ImmutableList.of(first, second, third, fourth, fifth, sixth)
  }
}