package omnia.data.structure.tuple

import omnia.data.structure.tuple.Tuples.AtLeastCouple
import omnia.data.structure.tuple.Tuples.AtMostCouple

interface Couple<A, B> : AtLeastCouple<A, B>, AtMostCouple {

  override fun count(): Int {
    return 2
  }

  override fun <R> mapFirst(mapper: (A) -> R): Couple<R, B>
  override fun <R> mapSecond(mapper: (B) -> R): Couple<A, R>
  override fun <T> append(`object`: T): Triple<A, B, T>
  override fun <C, D> append(other: Couple<C, D>): Quadruple<A, B, C, D>
  override fun <C, D, E> append(other: Triple<C, D, E>): Quintuple<A, B, C, D, E>
  override fun <C, D, E, F> append(other: Quadruple<C, D, E, F>): Sextuple<A, B, C, D, E, F>
  override fun <C, D, E, F, G> append(other: Quintuple<C, D, E, F, G>): Septuple<A, B, C, D, E, F, G>
  override fun <C, D, E, F, G, H> append(other: Sextuple<C, D, E, F, G, H>): Octuple<A, B, C, D, E, F, G, H>
  override fun <C, D, E, F, G, H, I> append(other: Septuple<C, D, E, F, G, H, I>): Nonuple<A, B, C, D, E, F, G, H, I>
  override fun <C, D, E, F, G, H, I, J> append(other: Octuple<C, D, E, F, G, H, I, J>): Decuple<A, B, C, D, E, F, G, H, I, J>
}