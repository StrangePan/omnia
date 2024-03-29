package omnia.data.structure.tuple

import omnia.data.structure.immutable.ImmutableList
import omnia.data.structure.tuple.Tuples.AtMostQuadruplet

class Quadruplet<T : Any>(first: T, second: T, third: T, fourth: T) :
  Quadruple<T, T, T, T>(first, second, third, fourth), AtMostQuadruplet<T> {

  override fun <R : Any> map(mapper: (T) -> R): Quadruplet<R> {
    return Tuplet.of(
      mapper(first),
      mapper(second),
      mapper(third),
      mapper(fourth)
    )
  }

  override fun dropFirst(): Triplet<T> {
    return Tuplet.of(second, third, fourth)
  }

  override fun dropSecond(): Triplet<T> {
    return Tuplet.of(first, third, fourth)
  }

  override fun dropThird(): Triplet<T> {
    return Tuplet.of(first, second, fourth)
  }

  override fun dropFourth(): Triplet<T> {
    return Tuplet.of(first, second, third)
  }

  override fun concat(item: T): Quintuplet<T> {
    return Tuplet.of(first, second, third, fourth, item)
  }

  override fun concat(other: Couple<T, T>): Sextuplet<T> {
    return Tuplet.of(first, second, third, fourth, other.first, other.second)
  }

  override fun concat(other: Triple<T, T, T>): Septuplet<T> {
    return Tuplet.of(
      first,
      second,
      third,
      fourth,
      other.first,
      other.second,
      other.third
    )
  }

  override fun concat(other: Quadruple<T, T, T, T>): Octuplet<T> {
    return Tuplet.of(
      first,
      second,
      third,
      fourth,
      other.first,
      other.second,
      other.third,
      other.fourth
    )
  }

  override fun concat(other: Quintuple<T, T, T, T, T>): Nonuplet<T> {
    return Tuplet.of(
      first,
      second,
      third,
      fourth,
      other.first,
      other.second,
      other.third,
      other.fourth,
      other.fifth
    )
  }

  override fun concat(other: Sextuple<T, T, T, T, T, T>): Decuplet<T> {
    return Tuplet.of(
      first,
      second,
      third,
      fourth,
      other.first,
      other.second,
      other.third,
      other.fourth,
      other.fifth,
      other.sixth
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
    return ImmutableList.of(first, second, third, fourth)
  }
}