package omnia.data.structure.tuple

import omnia.data.structure.tuple.Tuples.AtMostQuadruplet

interface Quadruplet<T : Any> : Quadruple<T, T, T, T>, AtMostQuadruplet<T> {

  override fun <R : Any> map(mapper: (T) -> R): Quadruplet<R>
  override fun dropFirst(): Triplet<T>
  override fun dropSecond(): Triplet<T>
  override fun dropThird(): Triplet<T>
  override fun dropFourth(): Triplet<T>
  override fun concat(`object`: T): Quintuplet<T>
  override fun concat(other: Couple<T, T>): Sextuplet<T>
  override fun concat(other: Triple<T, T, T>): Septuplet<T>
  override fun concat(other: Quadruple<T, T, T, T>): Octuplet<T>
  override fun concat(other: Quintuple<T, T, T, T, T>): Nonuplet<T>
  override fun concat(other: Sextuple<T, T, T, T, T, T>): Decuplet<T>

  companion object {

    fun <T : Any> of(first: T, second: T, third: T, fourth: T): Quadruplet<T> {
      return ImmutableQuadruplet(first, second, third, fourth)
    }
  }
}