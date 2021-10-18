package omnia.data.structure.tuple

import omnia.data.structure.tuple.Tuples.AtLeastQuintuple
import omnia.data.structure.tuple.Tuples.AtMostQuintuple

interface Quintuple<A, B, C, D, E> : AtLeastQuintuple<A, B, C, D, E>, AtMostQuintuple {

  override val count: Int get() {
    return 5
  }

  override fun <R> mapFirst(mapper: (A) -> R): Quintuple<R, B, C, D, E>
  override fun <R> mapSecond(mapper: (B) -> R): Quintuple<A, R, C, D, E>
  override fun <R> mapThird(mapper: (C) -> R): Quintuple<A, B, R, D, E>
  override fun <R> mapFourth(mapper: (D) -> R): Quintuple<A, B, C, R, E>
  override fun <R> mapFifth(mapper: (E) -> R): Quintuple<A, B, C, D, R>
  override fun dropFirst(): Quadruple<B, C, D, E>
  override fun dropSecond(): Quadruple<A, C, D, E>
  override fun dropThird(): Quadruple<A, B, D, E>
  override fun dropFourth(): Quadruple<A, B, C, E>
  override fun dropFifth(): Quadruple<A, B, C, D>
  override fun <T> append(`object`: T): Sextuple<A, B, C, D, E, T>
  override fun <F, G> append(other: Couple<F, G>): Septuple<A, B, C, D, E, F, G>
  override fun <F, G, H> append(other: Triple<F, G, H>): Octuple<A, B, C, D, E, F, G, H>
  override fun <F, G, H, I> append(other: Quadruple<F, G, H, I>): Nonuple<A, B, C, D, E, F, G, H, I>
  override fun <F, G, H, I, J> append(other: Quintuple<F, G, H, I, J>): Decuple<A, B, C, D, E, F, G, H, I, J>
}