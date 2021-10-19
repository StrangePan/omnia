package omnia.data.structure.tuple

import omnia.data.structure.immutable.ImmutableList

internal class ImmutableCouplet<T : Any>(first: T, second: T) : ImmutableCouple<T, T>(first, second),
    Couplet<T> {

  override fun <R : Any> map(mapper: (T) -> R): Couplet<R> {
    return Tuplet.of(mapper(first), mapper(second))
  }

  override fun concat(`object`: T): Triplet<T> {
    return Tuplet.of(first, second, `object`)
  }

  override fun concat(other: Couple<T, T>): Quadruplet<T> {
    return Tuplet.of(first, second, other.first, other.second)
  }

  override fun concat(other: Triple<T, T, T>): Quintuplet<T> {
    return Tuplet.of(first, second, other.first, other.second, other.third)
  }

  override fun concat(other: Quadruple<T, T, T, T>): Sextuplet<T> {
    return Tuplet.of(
      first,
      second,
        other.first,
        other.second,
        other.third,
        other.fourth
    )
  }

  override fun concat(other: Quintuple<T, T, T, T, T>): Septuplet<T> {
    return Tuplet.of(
      first,
      second,
        other.first,
        other.second,
        other.third,
        other.fourth,
        other.fifth
    )
  }

  override fun concat(other: Sextuple<T, T, T, T, T, T>): Octuplet<T> {
    return Tuplet.of(
      first,
      second,
        other.first,
        other.second,
        other.third,
        other.fourth,
        other.fifth,
        other.sixth
    )
  }

  override fun concat(other: Septuple<T, T, T, T, T, T, T>): Nonuplet<T> {
    return Tuplet.of(
      first,
      second,
        other.first,
        other.second,
        other.third,
        other.fourth,
        other.fifth,
        other.sixth,
        other.seventh
    )
  }

  override fun concat(other: Octuple<T, T, T, T, T, T, T, T>): Decuplet<T> {
    return Tuplet.of(
      first,
      second,
        other.first,
        other.second,
        other.third,
        other.fourth,
        other.fifth,
        other.sixth,
        other.seventh,
        other.eighth
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
    return ImmutableList.of(first, second)
  }
}