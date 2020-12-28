package omnia.data.structure.tuple

import omnia.data.structure.tuple.Tuples.AtLeastSeptuple
import omnia.data.structure.tuple.Tuples.AtMostSeptuple
import java.util.function.Function

interface Septuple<A, B, C, D, E, F, G> : AtLeastSeptuple<A, B, C, D, E, F, G>, AtMostSeptuple {
  override fun count(): Int {
    return 7
  }

  override fun <R> mapFirst(mapper: Function<in A, out R>): Septuple<R, B, C, D, E, F, G>
  override fun <R> mapSecond(mapper: Function<in B, out R>): Septuple<A, R, C, D, E, F, G>
  override fun <R> mapThird(mapper: Function<in C, out R>): Septuple<A, B, R, D, E, F, G>
  override fun <R> mapFourth(mapper: Function<in D, out R>): Septuple<A, B, C, R, E, F, G>
  override fun <R> mapFifth(mapper: Function<in E, out R>): Septuple<A, B, C, D, R, F, G>
  override fun <R> mapSixth(mapper: Function<in F, out R>): Septuple<A, B, C, D, E, R, G>
  override fun <R> mapSeventh(mapper: Function<in G, out R>): Septuple<A, B, C, D, E, F, R>
  override fun dropFirst(): Sextuple<B, C, D, E, F, G>
  override fun dropSecond(): Sextuple<A, C, D, E, F, G>
  override fun dropThird(): Sextuple<A, B, D, E, F, G>
  override fun dropFourth(): Sextuple<A, B, C, E, F, G>
  override fun dropFifth(): Sextuple<A, B, C, D, F, G>
  override fun dropSixth(): Sextuple<A, B, C, D, E, G>
  override fun dropSeventh(): Sextuple<A, B, C, D, E, F>
  override fun <T> append(`object`: T): Octuple<A, B, C, D, E, F, G, T>
  override fun <H, I> append(other: Couple<H, I>): Nonuple<A, B, C, D, E, F, G, H, I>
  override fun <H, I, J> append(other: Triple<H, I, J>): Decuple<A, B, C, D, E, F, G, H, I, J>
}