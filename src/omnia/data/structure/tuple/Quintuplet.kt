package omnia.data.structure.tuple

import java.util.function.Function
import omnia.data.structure.tuple.Tuples.AtMostQuintuplet

interface Quintuplet<T> : Quintuple<T, T, T, T, T>, AtMostQuintuplet<T> {

  override fun <R> map(mapper: Function<in T, out R>): Quintuplet<R>
  override fun dropFirst(): Quadruplet<T>
  override fun dropSecond(): Quadruplet<T>
  override fun dropThird(): Quadruplet<T>
  override fun dropFourth(): Quadruplet<T>
  override fun dropFifth(): Quadruplet<T>
  override fun concat(`object`: T): Sextuplet<T>
  override fun concat(other: Couple<T, T>): Septuplet<T>
  override fun concat(other: Triple<T, T, T>): Octuplet<T>
  override fun concat(other: Quadruple<T, T, T, T>): Nonuplet<T>
  override fun concat(other: Quintuple<T, T, T, T, T>): Decuplet<T>

  companion object {

    fun <T> of(first: T, second: T, third: T, fourth: T, fifth: T): Quintuplet<T> {
      return ImmutableQuintuplet(first, second, third, fourth, fifth)
    }
  }
}