package omnia.data.structure.tuple

import java.util.function.Function
import omnia.data.structure.tuple.Tuples.AtLeastNonuple
import omnia.data.structure.tuple.Tuples.AtMostNonuple

interface Nonuple<A, B, C, D, E, F, G, H, I> : AtLeastNonuple<A, B, C, D, E, F, G, H, I>,
    AtMostNonuple {

  override fun count(): Int {
    return 9
  }

  override fun <R> mapFirst(mapper: Function<in A, out R>): Nonuple<R, B, C, D, E, F, G, H, I>
  override fun <R> mapSecond(mapper: Function<in B, out R>): Nonuple<A, R, C, D, E, F, G, H, I>
  override fun <R> mapThird(mapper: Function<in C, out R>): Nonuple<A, B, R, D, E, F, G, H, I>
  override fun <R> mapFourth(mapper: Function<in D, out R>): Nonuple<A, B, C, R, E, F, G, H, I>
  override fun <R> mapFifth(mapper: Function<in E, out R>): Nonuple<A, B, C, D, R, F, G, H, I>
  override fun <R> mapSixth(mapper: Function<in F, out R>): Nonuple<A, B, C, D, E, R, G, H, I>
  override fun <R> mapSeventh(mapper: Function<in G, out R>): Nonuple<A, B, C, D, E, F, R, H, I>
  override fun <R> mapEighth(mapper: Function<in H, out R>): Nonuple<A, B, C, D, E, F, G, R, I>
  override fun <R> mapNinth(mapper: Function<in I, out R>): Nonuple<A, B, C, D, E, F, G, H, R>
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