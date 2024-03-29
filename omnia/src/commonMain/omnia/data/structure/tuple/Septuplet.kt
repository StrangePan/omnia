package omnia.data.structure.tuple

import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.tuple.Tuples.AtMostSeptuplet

class Septuplet<T : Any>(
  first: T,
  second: T,
  third: T,
  fourth: T,
  fifth: T,
  sixth: T,
  seventh: T
) : Septuple<T, T, T, T, T, T, T>(first, second, third, fourth, fifth, sixth, seventh),
  AtMostSeptuplet<T> {

  override fun <R : Any> map(mapper: (T) -> R): Septuplet<R> {
    return Tuplet.of(
      mapper(first),
      mapper(second),
      mapper(third),
      mapper(fourth),
      mapper(fifth),
      mapper(sixth),
      mapper(seventh)
    )
  }

  override fun dropFirst(): Sextuplet<T> {
    return Tuplet.of(second, third, fourth, fifth, sixth, seventh)
  }

  override fun dropSecond(): Sextuplet<T> {
    return Tuplet.of(first, third, fourth, fifth, sixth, seventh)
  }

  override fun dropThird(): Sextuplet<T> {
    return Tuplet.of(first, second, fourth, fifth, sixth, seventh)
  }

  override fun dropFourth(): Sextuplet<T> {
    return Tuplet.of(first, second, third, fifth, sixth, seventh)
  }

  override fun dropFifth(): Sextuplet<T> {
    return Tuplet.of(first, second, third, fourth, sixth, seventh)
  }

  override fun dropSixth(): Sextuplet<T> {
    return Tuplet.of(first, second, third, fourth, fifth, seventh)
  }

  override fun dropSeventh(): Sextuplet<T> {
    return Tuplet.of(first, second, third, fourth, fifth, sixth)
  }

  override fun concat(item: T): Octuplet<T> {
    return Tuplet.of(first, second, third, fourth, fifth, sixth, seventh, item)
  }

  override fun concat(other: Couple<T, T>): Nonuplet<T> {
    return Tuplet.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      other.first,
      other.second
    )
  }

  override fun concat(other: Triple<T, T, T>): Decuplet<T> {
    return Tuplet.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      other.first,
      other.second,
      other.third
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
    return ImmutableList.of(first, second, third, fourth, fifth, sixth, seventh)
  }
}