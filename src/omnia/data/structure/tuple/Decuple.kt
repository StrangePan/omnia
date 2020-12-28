package omnia.data.structure.tuple

import omnia.data.structure.tuple.Tuples.AtLeastDecuple
import omnia.data.structure.tuple.Tuples.AtMostDecuple
import java.util.function.Function

interface Decuple<A, B, C, D, E, F, G, H, I, J> : AtLeastDecuple<A, B, C, D, E, F, G, H, I, J>, AtMostDecuple {
  override fun count(): Int {
    return 10
  }

  override fun <R> mapFirst(mapper: Function<in A, out R>): Decuple<R, B, C, D, E, F, G, H, I, J>
  override fun <R> mapSecond(mapper: Function<in B, out R>): Decuple<A, R, C, D, E, F, G, H, I, J>
  override fun <R> mapThird(mapper: Function<in C, out R>): Decuple<A, B, R, D, E, F, G, H, I, J>
  override fun <R> mapFourth(mapper: Function<in D, out R>): Decuple<A, B, C, R, E, F, G, H, I, J>
  override fun <R> mapFifth(mapper: Function<in E, out R>): Decuple<A, B, C, D, R, F, G, H, I, J>
  override fun <R> mapSixth(mapper: Function<in F, out R>): Decuple<A, B, C, D, E, R, G, H, I, J>
  override fun <R> mapSeventh(mapper: Function<in G, out R>): Decuple<A, B, C, D, E, F, R, H, I, J>
  override fun <R> mapEighth(mapper: Function<in H, out R>): Decuple<A, B, C, D, E, F, G, R, I, J>
  override fun <R> mapNinth(mapper: Function<in I, out R>): Decuple<A, B, C, D, E, F, G, H, R, J>
  override fun <R> mapTenth(mapper: Function<in J, out R>): Decuple<A, B, C, D, E, F, G, H, I, R>
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