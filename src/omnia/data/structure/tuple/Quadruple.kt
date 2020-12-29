package omnia.data.structure.tuple

import java.util.function.Function
import omnia.data.structure.tuple.Tuples.AtLeastQuadruple
import omnia.data.structure.tuple.Tuples.AtMostQuadruple

interface Quadruple<A, B, C, D> : AtLeastQuadruple<A, B, C, D>, AtMostQuadruple {

  override fun count(): Int {
    return 4
  }

  override fun <R> mapFirst(mapper: Function<in A, out R>): Quadruple<R, B, C, D>
  override fun <R> mapSecond(mapper: Function<in B, out R>): Quadruple<A, R, C, D>
  override fun <R> mapThird(mapper: Function<in C, out R>): Quadruple<A, B, R, D>
  override fun <R> mapFourth(mapper: Function<in D, out R>): Quadruple<A, B, C, R>
  override fun dropFirst(): Triple<B, C, D>
  override fun dropSecond(): Triple<A, C, D>
  override fun dropThird(): Triple<A, B, D>
  override fun dropFourth(): Triple<A, B, C>
  override fun <T> append(`object`: T): Quintuple<A, B, C, D, T>
  override fun <E, F> append(other: Couple<E, F>): Sextuple<A, B, C, D, E, F>
  override fun <E, F, G> append(other: Triple<E, F, G>): Septuple<A, B, C, D, E, F, G>
  override fun <E, F, G, H> append(other: Quadruple<E, F, G, H>): Octuple<A, B, C, D, E, F, G, H>
  override fun <E, F, G, H, I> append(other: Quintuple<E, F, G, H, I>): Nonuple<A, B, C, D, E, F, G, H, I>
  override fun <E, F, G, H, I, J> append(other: Sextuple<E, F, G, H, I, J>): Decuple<A, B, C, D, E, F, G, H, I, J>
}