package omnia.data.structure.tuple

import omnia.data.structure.tuple.Tuples.AtLeastOctuple
import omnia.data.structure.tuple.Tuples.AtMostOctuple
import java.util.function.Function

interface Octuple<A, B, C, D, E, F, G, H> : AtLeastOctuple<A, B, C, D, E, F, G, H>, AtMostOctuple {
  override fun count(): Int {
    return 8
  }

  override fun <R> mapFirst(mapper: Function<in A, out R>): Octuple<R, B, C, D, E, F, G, H>
  override fun <R> mapSecond(mapper: Function<in B, out R>): Octuple<A, R, C, D, E, F, G, H>
  override fun <R> mapThird(mapper: Function<in C, out R>): Octuple<A, B, R, D, E, F, G, H>
  override fun <R> mapFourth(mapper: Function<in D, out R>): Octuple<A, B, C, R, E, F, G, H>
  override fun <R> mapFifth(mapper: Function<in E, out R>): Octuple<A, B, C, D, R, F, G, H>
  override fun <R> mapSixth(mapper: Function<in F, out R>): Octuple<A, B, C, D, E, R, G, H>
  override fun <R> mapSeventh(mapper: Function<in G, out R>): Octuple<A, B, C, D, E, F, R, H>
  override fun <R> mapEighth(mapper: Function<in H, out R>): Octuple<A, B, C, D, E, F, G, R>
  override fun dropFirst(): Septuple<B, C, D, E, F, G, H>
  override fun dropSecond(): Septuple<A, C, D, E, F, G, H>
  override fun dropThird(): Septuple<A, B, D, E, F, G, H>
  override fun dropFourth(): Septuple<A, B, C, E, F, G, H>
  override fun dropFifth(): Septuple<A, B, C, D, F, G, H>
  override fun dropSixth(): Septuple<A, B, C, D, E, G, H>
  override fun dropSeventh(): Septuple<A, B, C, D, E, F, H>
  override fun dropEighth(): Septuple<A, B, C, D, E, F, G>
  override fun <T> append(`object`: T): Nonuple<A, B, C, D, E, F, G, H, T>
  override fun <I, J> append(other: Couple<I, J>): Decuple<A, B, C, D, E, F, G, H, I, J>
}