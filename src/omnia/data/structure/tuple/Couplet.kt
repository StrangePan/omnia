package omnia.data.structure.tuple

import omnia.data.structure.tuple.Tuples.AtMostCouplet
import java.util.function.Function

interface Couplet<T> : Couple<T, T>, AtMostCouplet<T> {
  override fun <R> map(mapper: Function<in T, out R>): Couplet<R>
  override fun concat(`object`: T): Triplet<T>
  override fun concat(other: Couple<T, T>): Quadruplet<T>
  override fun concat(other: Triple<T, T, T>): Quintuplet<T>
  override fun concat(other: Quadruple<T, T, T, T>): Sextuplet<T>
  override fun concat(other: Quintuple<T, T, T, T, T>): Septuplet<T>
  override fun concat(other: Sextuple<T, T, T, T, T, T>): Octuplet<T>
  override fun concat(other: Septuple<T, T, T, T, T, T, T>): Nonuplet<T>
  override fun concat(other: Octuple<T, T, T, T, T, T, T, T>): Decuplet<T>

  companion object {
    @kotlin.jvm.JvmStatic
    fun <T> of(first: T, second: T): Couplet<T> {
      return ImmutableCouplet(first, second)
    }
  }
}