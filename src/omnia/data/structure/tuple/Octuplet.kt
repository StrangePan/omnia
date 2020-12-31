package omnia.data.structure.tuple

import java.util.function.Function
import omnia.data.structure.tuple.Tuples.AtMostOctuplet

interface Octuplet<T> : Octuple<T, T, T, T, T, T, T, T>, AtMostOctuplet<T> {

  override fun <R> map(mapper: Function<in T, out R>): Octuplet<R>
  override fun dropFirst(): Septuplet<T>
  override fun dropSecond(): Septuplet<T>
  override fun dropThird(): Septuplet<T>
  override fun dropFourth(): Septuplet<T>
  override fun dropFifth(): Septuplet<T>
  override fun dropSixth(): Septuplet<T>
  override fun dropSeventh(): Septuplet<T>
  override fun dropEighth(): Septuplet<T>
  override fun concat(`object`: T): Nonuplet<T>
  override fun concat(other: Couple<T, T>): Decuplet<T>

  companion object {

    fun <T> of(
        first: T,
        second: T,
        third: T,
        fourth: T,
        fifth: T,
        sixth: T,
        seventh: T,
        eighth: T
    ): Octuplet<T> {
      return ImmutableOctuplet(first, second, third, fourth, fifth, sixth, seventh, eighth)
    }
  }
}