package omnia.data.structure.tuple

import omnia.data.structure.tuple.Tuples.AtLeastTriple
import omnia.data.structure.tuple.Tuples.AtMostTriple

interface Triple<A, B, C> : AtLeastTriple<A, B, C>, AtMostTriple {

  override fun count(): Int {
    return 3
  }

  override fun <R> mapFirst(mapper: (A) -> R): Triple<R, B, C>
  override fun <R> mapSecond(mapper: (B) -> R): Triple<A, R, C>
  override fun <R> mapThird(mapper: (C) -> R): Triple<A, B, R>
  override fun dropFirst(): Couple<B, C>
  override fun dropSecond(): Couple<A, C>
  override fun dropThird(): Couple<A, B>
  override fun <T> append(`object`: T): Quadruple<A, B, C, T>
  override fun <D, E> append(other: Couple<D, E>): Quintuple<A, B, C, D, E>
  override fun <D, E, F> append(other: Triple<D, E, F>): Sextuple<A, B, C, D, E, F>
  override fun <D, E, F, G> append(other: Quadruple<D, E, F, G>): Septuple<A, B, C, D, E, F, G>
  override fun <D, E, F, G, H> append(other: Quintuple<D, E, F, G, H>): Octuple<A, B, C, D, E, F, G, H>
  override fun <D, E, F, G, H, I> append(other: Sextuple<D, E, F, G, H, I>): Nonuple<A, B, C, D, E, F, G, H, I>
  override fun <D, E, F, G, H, I, J> append(other: Septuple<D, E, F, G, H, I, J>): Decuple<A, B, C, D, E, F, G, H, I, J>
}