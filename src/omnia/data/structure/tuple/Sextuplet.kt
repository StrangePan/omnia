package omnia.data.structure.tuple

import java.util.function.Function
import omnia.data.structure.tuple.Tuples.AtMostSextuplet

interface Sextuplet<T> : Sextuple<T, T, T, T, T, T>, AtMostSextuplet<T> {

  override fun <R> map(mapper: Function<in T, out R>): Sextuplet<R>
  override fun dropFirst(): Quintuplet<T>
  override fun dropSecond(): Quintuplet<T>
  override fun dropThird(): Quintuplet<T>
  override fun dropFourth(): Quintuplet<T>
  override fun dropFifth(): Quintuplet<T>
  override fun dropSixth(): Quintuplet<T>
  override fun concat(`object`: T): Septuplet<T>
  override fun concat(other: Couple<T, T>): Octuplet<T>
  override fun concat(other: Triple<T, T, T>): Nonuplet<T>
  override fun concat(other: Quadruple<T, T, T, T>): Decuplet<T>

  companion object {

    fun <T> of(first: T, second: T, third: T, fourth: T, fifth: T, sixth: T): Sextuplet<T> {
      return ImmutableSextuplet(first, second, third, fourth, fifth, sixth)
    }
  }
}