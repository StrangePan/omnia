package omnia.data.structure.tuple

import java.util.function.Function
import omnia.data.structure.tuple.Tuples.AtMostSeptuplet

interface Septuplet<T> : Septuple<T, T, T, T, T, T, T>, AtMostSeptuplet<T> {

  override fun <R> map(mapper: Function<in T, out R>): Septuplet<R>
  override fun dropFirst(): Sextuplet<T>
  override fun dropSecond(): Sextuplet<T>
  override fun dropThird(): Sextuplet<T>
  override fun dropFourth(): Sextuplet<T>
  override fun dropFifth(): Sextuplet<T>
  override fun dropSixth(): Sextuplet<T>
  override fun dropSeventh(): Sextuplet<T>
  override fun concat(`object`: T): Octuplet<T>
  override fun concat(other: Couple<T, T>): Nonuplet<T>
  override fun concat(other: Triple<T, T, T>): Decuplet<T>

  companion object {

    fun <T> of(
        first: T,
        second: T,
        third: T,
        fourth: T,
        fifth: T,
        sixth: T,
        seventh: T
    ): Septuplet<T> {
      return ImmutableSeptuplet(first, second, third, fourth, fifth, sixth, seventh)
    }
  }
}