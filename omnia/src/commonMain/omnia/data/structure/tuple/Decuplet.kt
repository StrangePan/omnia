package omnia.data.structure.tuple

import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.tuple.Tuples.AtMostDecuplet

class Decuplet<T : Any>(
  first: T,
  second: T,
  third: T,
  fourth: T,
  fifth: T,
  sixth: T,
  seventh: T,
  eighth: T,
  ninth: T,
  tenth: T
) : Decuple<T, T, T, T, T, T, T, T, T, T>(
  first,
  second,
  third,
  fourth,
  fifth,
  sixth,
  seventh,
  eighth,
  ninth,
  tenth
), AtMostDecuplet<T> {

  override fun <R : Any> map(mapper: (T) -> R): Decuplet<R> {
    return Tuplet.of(
      mapper(first),
      mapper(second),
      mapper(third),
      mapper(fourth),
      mapper(fifth),
      mapper(sixth),
      mapper(seventh),
      mapper(eighth),
      mapper(ninth),
      mapper(tenth)
    )
  }

  override fun dropFirst(): Nonuplet<T> {
    return Tuplet.of(
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
      ninth,
      tenth
    )
  }

  override fun dropSecond(): Nonuplet<T> {
    return Tuplet.of(
      first,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
      ninth,
      tenth
    )
  }

  override fun dropThird(): Nonuplet<T> {
    return Tuplet.of(
      first,
      second,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
      ninth,
      tenth
    )
  }

  override fun dropFourth(): Nonuplet<T> {
    return Tuplet.of(
      first,
      second,
      third,
      fifth,
      sixth,
      seventh,
      eighth,
      ninth,
      tenth
    )
  }

  override fun dropFifth(): Nonuplet<T> {
    return Tuplet.of(
      first,
      second,
      third,
      fourth,
      sixth,
      seventh,
      eighth,
      ninth,
      tenth
    )
  }

  override fun dropSixth(): Nonuplet<T> {
    return Tuplet.of(
      first,
      second,
      third,
      fourth,
      fifth,
      seventh,
      eighth,
      ninth,
      tenth
    )
  }

  override fun dropSeventh(): Nonuplet<T> {
    return Tuplet.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      eighth,
      ninth,
      tenth
    )
  }

  override fun dropEighth(): Nonuplet<T> {
    return Tuplet.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      ninth,
      tenth
    )
  }

  override fun dropNinth(): Nonuplet<T> {
    return Tuplet.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
      tenth
    )
  }

  override fun dropTenth(): Nonuplet<T> {
    return Tuplet.of(
      first,
      second,
      third,
      fourth,
      fifth,
      sixth,
      seventh,
      eighth,
      ninth
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
      eighth,
      ninth,
      tenth
    )
  }
}