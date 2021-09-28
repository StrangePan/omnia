package omnia.data.structure.tuple

import java.util.function.Function
import omnia.data.structure.tuple.Tuples.AtMostNonuplet

interface Nonuplet<T : Any> : Nonuple<T, T, T, T, T, T, T, T, T>, AtMostNonuplet<T> {

  override fun <R : Any> map(mapper: Function<in T, out R>): Nonuplet<R>
  override fun dropFirst(): Octuplet<T>
  override fun dropSecond(): Octuplet<T>
  override fun dropThird(): Octuplet<T>
  override fun dropFourth(): Octuplet<T>
  override fun dropFifth(): Octuplet<T>
  override fun dropSixth(): Octuplet<T>
  override fun dropSeventh(): Octuplet<T>
  override fun dropEighth(): Octuplet<T>
  override fun dropNinth(): Octuplet<T>
  override fun concat(`object`: T): Decuplet<T>

  companion object {

    fun <T : Any> of(
        first: T,
        second: T,
        third: T,
        fourth: T,
        fifth: T,
        sixth: T,
        seventh: T,
        eighth: T,
        ninth: T
    ): Nonuplet<T> {
      return ImmutableNonuplet(first, second, third, fourth, fifth, sixth, seventh, eighth, ninth)
    }
  }
}