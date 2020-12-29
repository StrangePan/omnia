package omnia.data.structure.tuple

import java.util.function.Function
import omnia.data.structure.tuple.Tuples.AtMostDecuplet

interface Decuplet<T> : Decuple<T, T, T, T, T, T, T, T, T, T>, AtMostDecuplet<T> {

  override fun <R> map(mapper: Function<in T, out R>): Decuplet<R>
  override fun dropFirst(): Nonuplet<T>
  override fun dropSecond(): Nonuplet<T>
  override fun dropThird(): Nonuplet<T>
  override fun dropFourth(): Nonuplet<T>
  override fun dropFifth(): Nonuplet<T>
  override fun dropSixth(): Nonuplet<T>
  override fun dropSeventh(): Nonuplet<T>
  override fun dropEighth(): Nonuplet<T>
  override fun dropNinth(): Nonuplet<T>
  override fun dropTenth(): Nonuplet<T>

  companion object {

    fun <T> of(
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
    ): Decuplet<T> {
      return ImmutableDecuplet(
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
}