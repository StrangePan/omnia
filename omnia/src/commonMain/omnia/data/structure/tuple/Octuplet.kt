package omnia.data.structure.tuple

import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.tuple.Tuples.AtMostOctuplet

class Octuplet<T : Any>(
  first: T,
  second: T,
  third: T,
  fourth: T,
  fifth: T,
  sixth: T,
  seventh: T,
  eighth: T
) : Octuple<T, T, T, T, T, T, T, T>(
  first,
  second,
  third,
  fourth,
  fifth,
  sixth,
  seventh,
  eighth
), AtMostOctuplet<T> {

  override fun <R : Any> map(mapper: (T) -> R): Octuplet<R> {
    return Tuplet.of(
      mapper(first),
      mapper(second),
      mapper(third),
      mapper(fourth),
      mapper(fifth),
      mapper(sixth),
      mapper(seventh),
      mapper(eighth)
    )
  }

  override fun dropFirst(): Septuplet<T> {
    return Tuplet.of(second, third, fourth, fifth, sixth, seventh, eighth)
  }

  override fun dropSecond(): Septuplet<T> {
    return Tuplet.of(first, third, fourth, fifth, sixth, seventh, eighth)
  }

  override fun dropThird(): Septuplet<T> {
    return Tuplet.of(first, second, fourth, fifth, sixth, seventh, eighth)
  }

  override fun dropFourth(): Septuplet<T> {
    return Tuplet.of(first, second, third, fifth, sixth, seventh, eighth)
  }

  override fun dropFifth(): Septuplet<T> {
    return Tuplet.of(first, second, third, fourth, sixth, seventh, eighth)
  }

  override fun dropSixth(): Septuplet<T> {
    return Tuplet.of(first, second, third, fourth, fifth, seventh, eighth)
  }

  override fun dropSeventh(): Septuplet<T> {
    return Tuplet.of(first, second, third, fourth, fifth, sixth, eighth)
  }

  override fun dropEighth(): Septuplet<T> {
    return Tuplet.of(first, second, third, fourth, fifth, sixth, seventh)
  }

  override fun concat(item: T): Nonuplet<T> {
    return Tuplet.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
      item
    )
  }

  override fun concat(other: Couple<T, T>): Decuplet<T> {
    return Tuplet.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
      other.first,
      other.second
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
    return ImmutableList.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth
    )
  }
}