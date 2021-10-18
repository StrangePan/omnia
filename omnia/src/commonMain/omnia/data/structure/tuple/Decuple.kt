package omnia.data.structure.tuple

import omnia.data.structure.tuple.Tuples.AtLeastDecuple
import omnia.data.structure.tuple.Tuples.AtMostDecuple

interface Decuple<A, B, C, D, E, F, G, H, I, J> : AtLeastDecuple<A, B, C, D, E, F, G, H, I, J>,
    AtMostDecuple {

  override val count: Int get() {
    return 10
  }

  override fun <R> mapFirst(mapper: (A) -> R): Decuple<R, B, C, D, E, F, G, H, I, J>
  override fun <R> mapSecond(mapper: (B) -> R): Decuple<A, R, C, D, E, F, G, H, I, J>
  override fun <R> mapThird(mapper: (C) -> R): Decuple<A, B, R, D, E, F, G, H, I, J>
  override fun <R> mapFourth(mapper: (D) -> R): Decuple<A, B, C, R, E, F, G, H, I, J>
  override fun <R> mapFifth(mapper: (E) -> R): Decuple<A, B, C, D, R, F, G, H, I, J>
  override fun <R> mapSixth(mapper: (F) -> R): Decuple<A, B, C, D, E, R, G, H, I, J>
  override fun <R> mapSeventh(mapper: (G) -> R): Decuple<A, B, C, D, E, F, R, H, I, J>
  override fun <R> mapEighth(mapper: (H) -> R): Decuple<A, B, C, D, E, F, G, R, I, J>
  override fun <R> mapNinth(mapper: (I) -> R): Decuple<A, B, C, D, E, F, G, H, R, J>
  override fun <R> mapTenth(mapper: (J) -> R): Decuple<A, B, C, D, E, F, G, H, I, R>
  override fun dropFirst(): Nonuple<B, C, D, E, F, G, H, I, J>
  override fun dropSecond(): Nonuple<A, C, D, E, F, G, H, I, J>
  override fun dropThird(): Nonuple<A, B, D, E, F, G, H, I, J>
  override fun dropFourth(): Nonuple<A, B, C, E, F, G, H, I, J>
  override fun dropFifth(): Nonuple<A, B, C, D, F, G, H, I, J>
  override fun dropSixth(): Nonuple<A, B, C, D, E, G, H, I, J>
  override fun dropSeventh(): Nonuple<A, B, C, D, E, F, H, I, J>
  override fun dropEighth(): Nonuple<A, B, C, D, E, F, G, I, J>
  override fun dropNinth(): Nonuple<A, B, C, D, E, F, G, H, J>
  override fun dropTenth(): Nonuple<A, B, C, D, E, F, G, H, I>
}