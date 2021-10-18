package omnia.data.structure.tuple

import omnia.data.structure.tuple.Tuples.AtLeastNonuple
import omnia.data.structure.tuple.Tuples.AtMostNonuple

interface Nonuple<A, B, C, D, E, F, G, H, I> : AtLeastNonuple<A, B, C, D, E, F, G, H, I>,
    AtMostNonuple {

  override val count: Int get() {
    return 9
  }

  override fun <R> mapFirst(mapper: (A) -> R): Nonuple<R, B, C, D, E, F, G, H, I>
  override fun <R> mapSecond(mapper: (B) -> R): Nonuple<A, R, C, D, E, F, G, H, I>
  override fun <R> mapThird(mapper: (C) -> R): Nonuple<A, B, R, D, E, F, G, H, I>
  override fun <R> mapFourth(mapper: (D) -> R): Nonuple<A, B, C, R, E, F, G, H, I>
  override fun <R> mapFifth(mapper: (E) -> R): Nonuple<A, B, C, D, R, F, G, H, I>
  override fun <R> mapSixth(mapper: (F) -> R): Nonuple<A, B, C, D, E, R, G, H, I>
  override fun <R> mapSeventh(mapper: (G) -> R): Nonuple<A, B, C, D, E, F, R, H, I>
  override fun <R> mapEighth(mapper: (H) -> R): Nonuple<A, B, C, D, E, F, G, R, I>
  override fun <R> mapNinth(mapper: (I) -> R): Nonuple<A, B, C, D, E, F, G, H, R>
  override fun dropFirst(): Octuple<B, C, D, E, F, G, H, I>
  override fun dropSecond(): Octuple<A, C, D, E, F, G, H, I>
  override fun dropThird(): Octuple<A, B, D, E, F, G, H, I>
  override fun dropFourth(): Octuple<A, B, C, E, F, G, H, I>
  override fun dropFifth(): Octuple<A, B, C, D, F, G, H, I>
  override fun dropSixth(): Octuple<A, B, C, D, E, G, H, I>
  override fun dropSeventh(): Octuple<A, B, C, D, E, F, H, I>
  override fun dropEighth(): Octuple<A, B, C, D, E, F, G, I>
  override fun dropNinth(): Octuple<A, B, C, D, E, F, G, H>
  override fun <T> append(`object`: T): Decuple<A, B, C, D, E, F, G, H, I, T>
}