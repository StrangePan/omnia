package omnia.data.structure.tuple

import java.util.function.Function

class Tuples {
  interface AtLeastCouple<A, B> : Tuple {
    fun first(): A
    fun second(): B
    fun <R> mapFirst(mapper: Function<in A, out R>): AtLeastCouple<R, B>
    fun <R> mapSecond(mapper: Function<in B, out R>): AtLeastCouple<A, R>
  }

  interface AtLeastTriple<A, B, C> : AtLeastCouple<A, B> {
    fun third(): C
    override fun <R> mapFirst(mapper: Function<in A, out R>): AtLeastTriple<R, B, C>
    override fun <R> mapSecond(mapper: Function<in B, out R>): AtLeastTriple<A, R, C>
    fun <R> mapThird(mapper: Function<in C, out R>): AtLeastTriple<A, B, R>
    fun dropFirst(): AtLeastCouple<B, C>
    fun dropSecond(): AtLeastCouple<A, C>
    fun dropThird(): AtLeastCouple<A, B>
  }

  interface AtLeastQuadruple<A, B, C, D> : AtLeastTriple<A, B, C> {
    fun fourth(): D
    override fun <R> mapFirst(mapper: Function<in A, out R>): AtLeastQuadruple<R, B, C, D>
    override fun <R> mapSecond(mapper: Function<in B, out R>): AtLeastQuadruple<A, R, C, D>
    override fun <R> mapThird(mapper: Function<in C, out R>): AtLeastQuadruple<A, B, R, D>
    fun <R> mapFourth(mapper: Function<in D, out R>): AtLeastQuadruple<A, B, C, R>
    override fun dropFirst(): AtLeastTriple<B, C, D>
    override fun dropSecond(): AtLeastTriple<A, C, D>
    override fun dropThird(): AtLeastTriple<A, B, D>
    fun dropFourth(): AtLeastTriple<A, B, C>
  }

  interface AtLeastQuintuple<A, B, C, D, E> : AtLeastQuadruple<A, B, C, D> {
    fun fifth(): E
    override fun <R> mapFirst(mapper: Function<in A, out R>): AtLeastQuintuple<R, B, C, D, E>
    override fun <R> mapSecond(mapper: Function<in B, out R>): AtLeastQuintuple<A, R, C, D, E>
    override fun <R> mapThird(mapper: Function<in C, out R>): AtLeastQuintuple<A, B, R, D, E>
    override fun <R> mapFourth(mapper: Function<in D, out R>): AtLeastQuintuple<A, B, C, R, E>
    fun <R> mapFifth(mapper: Function<in E, out R>): AtLeastQuintuple<A, B, C, D, R>
    override fun dropFirst(): AtLeastQuadruple<B, C, D, E>
    override fun dropSecond(): AtLeastQuadruple<A, C, D, E>
    override fun dropThird(): AtLeastQuadruple<A, B, D, E>
    override fun dropFourth(): AtLeastQuadruple<A, B, C, E>
    fun dropFifth(): AtLeastQuadruple<A, B, C, D>
  }

  interface AtLeastSextuple<A, B, C, D, E, F> : AtLeastQuintuple<A, B, C, D, E> {
    fun sixth(): F
    override fun <R> mapFirst(mapper: Function<in A, out R>): AtLeastSextuple<R, B, C, D, E, F>
    override fun <R> mapSecond(mapper: Function<in B, out R>): AtLeastSextuple<A, R, C, D, E, F>
    override fun <R> mapThird(mapper: Function<in C, out R>): AtLeastSextuple<A, B, R, D, E, F>
    override fun <R> mapFourth(mapper: Function<in D, out R>): AtLeastSextuple<A, B, C, R, E, F>
    override fun <R> mapFifth(mapper: Function<in E, out R>): AtLeastSextuple<A, B, C, D, R, F>
    fun <R> mapSixth(mapper: Function<in F, out R>): AtLeastSextuple<A, B, C, D, E, R>
    override fun dropFirst(): AtLeastQuintuple<B, C, D, E, F>
    override fun dropSecond(): AtLeastQuintuple<A, C, D, E, F>
    override fun dropThird(): AtLeastQuintuple<A, B, D, E, F>
    override fun dropFourth(): AtLeastQuintuple<A, B, C, E, F>
    override fun dropFifth(): AtLeastQuintuple<A, B, C, D, F>
    fun dropSixth(): AtLeastQuintuple<A, B, C, D, E>
  }

  interface AtLeastSeptuple<A, B, C, D, E, F, G> : AtLeastSextuple<A, B, C, D, E, F> {
    fun seventh(): G
    override fun <R> mapFirst(mapper: Function<in A, out R>): AtLeastSeptuple<R, B, C, D, E, F, G>
    override fun <R> mapSecond(mapper: Function<in B, out R>): AtLeastSeptuple<A, R, C, D, E, F, G>
    override fun <R> mapThird(mapper: Function<in C, out R>): AtLeastSeptuple<A, B, R, D, E, F, G>
    override fun <R> mapFourth(mapper: Function<in D, out R>): AtLeastSeptuple<A, B, C, R, E, F, G>
    override fun <R> mapFifth(mapper: Function<in E, out R>): AtLeastSeptuple<A, B, C, D, R, F, G>
    override fun <R> mapSixth(mapper: Function<in F, out R>): AtLeastSeptuple<A, B, C, D, E, R, G>
    fun <R> mapSeventh(mapper: Function<in G, out R>): AtLeastSeptuple<A, B, C, D, E, F, R>
    override fun dropFirst(): AtLeastSextuple<B, C, D, E, F, G>
    override fun dropSecond(): AtLeastSextuple<A, C, D, E, F, G>
    override fun dropThird(): AtLeastSextuple<A, B, D, E, F, G>
    override fun dropFourth(): AtLeastSextuple<A, B, C, E, F, G>
    override fun dropFifth(): AtLeastSextuple<A, B, C, D, F, G>
    override fun dropSixth(): AtLeastSextuple<A, B, C, D, E, G>
    fun dropSeventh(): AtLeastSextuple<A, B, C, D, E, F>
  }

  interface AtLeastOctuple<A, B, C, D, E, F, G, H> : AtLeastSeptuple<A, B, C, D, E, F, G> {
    fun eighth(): H
    override fun <R> mapFirst(mapper: Function<in A, out R>): AtLeastOctuple<R, B, C, D, E, F, G, H>
    override fun <R> mapSecond(mapper: Function<in B, out R>): AtLeastOctuple<A, R, C, D, E, F, G, H>
    override fun <R> mapThird(mapper: Function<in C, out R>): AtLeastOctuple<A, B, R, D, E, F, G, H>
    override fun <R> mapFourth(mapper: Function<in D, out R>): AtLeastOctuple<A, B, C, R, E, F, G, H>
    override fun <R> mapFifth(mapper: Function<in E, out R>): AtLeastOctuple<A, B, C, D, R, F, G, H>
    override fun <R> mapSixth(mapper: Function<in F, out R>): AtLeastOctuple<A, B, C, D, E, R, G, H>
    override fun <R> mapSeventh(mapper: Function<in G, out R>): AtLeastOctuple<A, B, C, D, E, F, R, H>
    fun <R> mapEighth(mapper: Function<in H, out R>): AtLeastOctuple<A, B, C, D, E, F, G, R>
    override fun dropFirst(): AtLeastSeptuple<B, C, D, E, F, G, H>
    override fun dropSecond(): AtLeastSeptuple<A, C, D, E, F, G, H>
    override fun dropThird(): AtLeastSeptuple<A, B, D, E, F, G, H>
    override fun dropFourth(): AtLeastSeptuple<A, B, C, E, F, G, H>
    override fun dropFifth(): AtLeastSeptuple<A, B, C, D, F, G, H>
    override fun dropSixth(): AtLeastSeptuple<A, B, C, D, E, G, H>
    override fun dropSeventh(): AtLeastSeptuple<A, B, C, D, E, F, H>
    fun dropEighth(): AtLeastSeptuple<A, B, C, D, E, F, G>
  }

  interface AtLeastNonuple<A, B, C, D, E, F, G, H, I> : AtLeastOctuple<A, B, C, D, E, F, G, H> {
    fun ninth(): I
    override fun <R> mapFirst(mapper: Function<in A, out R>): AtLeastNonuple<R, B, C, D, E, F, G, H, I>
    override fun <R> mapSecond(mapper: Function<in B, out R>): AtLeastNonuple<A, R, C, D, E, F, G, H, I>
    override fun <R> mapThird(mapper: Function<in C, out R>): AtLeastNonuple<A, B, R, D, E, F, G, H, I>
    override fun <R> mapFourth(mapper: Function<in D, out R>): AtLeastNonuple<A, B, C, R, E, F, G, H, I>
    override fun <R> mapFifth(mapper: Function<in E, out R>): AtLeastNonuple<A, B, C, D, R, F, G, H, I>
    override fun <R> mapSixth(mapper: Function<in F, out R>): AtLeastNonuple<A, B, C, D, E, R, G, H, I>
    override fun <R> mapSeventh(mapper: Function<in G, out R>): AtLeastNonuple<A, B, C, D, E, F, R, H, I>
    override fun <R> mapEighth(mapper: Function<in H, out R>): AtLeastNonuple<A, B, C, D, E, F, G, R, I>
    fun <R> mapNinth(mapper: Function<in I, out R>): AtLeastNonuple<A, B, C, D, E, F, G, H, R>
    override fun dropFirst(): AtLeastOctuple<B, C, D, E, F, G, H, I>
    override fun dropSecond(): AtLeastOctuple<A, C, D, E, F, G, H, I>
    override fun dropThird(): AtLeastOctuple<A, B, D, E, F, G, H, I>
    override fun dropFourth(): AtLeastOctuple<A, B, C, E, F, G, H, I>
    override fun dropFifth(): AtLeastOctuple<A, B, C, D, F, G, H, I>
    override fun dropSixth(): AtLeastOctuple<A, B, C, D, E, G, H, I>
    override fun dropSeventh(): AtLeastOctuple<A, B, C, D, E, F, H, I>
    override fun dropEighth(): AtLeastOctuple<A, B, C, D, E, F, G, I>
    fun dropNinth(): AtLeastOctuple<A, B, C, D, E, F, G, H>
  }

  interface AtLeastDecuple<A, B, C, D, E, F, G, H, I, J> : AtLeastNonuple<A, B, C, D, E, F, G, H, I> {
    fun tenth(): J
    override fun <R> mapFirst(mapper: Function<in A, out R>): AtLeastDecuple<R, B, C, D, E, F, G, H, I, J>
    override fun <R> mapSecond(mapper: Function<in B, out R>): AtLeastDecuple<A, R, C, D, E, F, G, H, I, J>
    override fun <R> mapThird(mapper: Function<in C, out R>): AtLeastDecuple<A, B, R, D, E, F, G, H, I, J>
    override fun <R> mapFourth(mapper: Function<in D, out R>): AtLeastDecuple<A, B, C, R, E, F, G, H, I, J>
    override fun <R> mapFifth(mapper: Function<in E, out R>): AtLeastDecuple<A, B, C, D, R, F, G, H, I, J>
    override fun <R> mapSixth(mapper: Function<in F, out R>): AtLeastDecuple<A, B, C, D, E, R, G, H, I, J>
    override fun <R> mapSeventh(mapper: Function<in G, out R>): AtLeastDecuple<A, B, C, D, E, F, R, H, I, J>
    override fun <R> mapEighth(mapper: Function<in H, out R>): AtLeastDecuple<A, B, C, D, E, F, G, R, I, J>
    override fun <R> mapNinth(mapper: Function<in I, out R>): AtLeastDecuple<A, B, C, D, E, F, G, H, R, J>
    fun <R> mapTenth(mapper: Function<in J, out R>): AtLeastDecuple<A, B, C, D, E, F, G, H, I, R>
    override fun dropFirst(): AtLeastNonuple<B, C, D, E, F, G, H, I, J>
    override fun dropSecond(): AtLeastNonuple<A, C, D, E, F, G, H, I, J>
    override fun dropThird(): AtLeastNonuple<A, B, D, E, F, G, H, I, J>
    override fun dropFourth(): AtLeastNonuple<A, B, C, E, F, G, H, I, J>
    override fun dropFifth(): AtLeastNonuple<A, B, C, D, F, G, H, I, J>
    override fun dropSixth(): AtLeastNonuple<A, B, C, D, E, G, H, I, J>
    override fun dropSeventh(): AtLeastNonuple<A, B, C, D, E, F, H, I, J>
    override fun dropEighth(): AtLeastNonuple<A, B, C, D, E, F, G, I, J>
    override fun dropNinth(): AtLeastNonuple<A, B, C, D, E, F, G, H, J>
    fun dropTenth(): AtLeastNonuple<A, B, C, D, E, F, G, H, I>
  }

  interface AtMostCouple : AtMostTriple {
    override fun <T> append(`object`: T): AtMostTriple
    override fun <C, D> append(other: Couple<C, D>): AtMostQuadruple
    override fun <C, D, E> append(other: Triple<C, D, E>): AtMostQuintuple
    override fun <C, D, E, F> append(other: Quadruple<C, D, E, F>): AtMostSextuple
    override fun <C, D, E, F, G> append(other: Quintuple<C, D, E, F, G>): AtMostSeptuple
    override fun <C, D, E, F, G, H> append(other: Sextuple<C, D, E, F, G, H>): AtMostOctuple
    override fun <C, D, E, F, G, H, I> append(other: Septuple<C, D, E, F, G, H, I>): AtMostNonuple
    fun <C, D, E, F, G, H, I, J> append(other: Octuple<C, D, E, F, G, H, I, J>): AtMostDecuple
  }

  interface AtMostTriple : AtMostQuadruple {
    override fun <T> append(`object`: T): AtMostQuadruple
    override fun <D, E> append(other: Couple<D, E>): AtMostQuintuple
    override fun <D, E, F> append(other: Triple<D, E, F>): AtMostSextuple
    override fun <D, E, F, G> append(other: Quadruple<D, E, F, G>): AtMostSeptuple
    override fun <D, E, F, G, H> append(other: Quintuple<D, E, F, G, H>): AtMostOctuple
    override fun <D, E, F, G, H, I> append(other: Sextuple<D, E, F, G, H, I>): AtMostNonuple
    fun <D, E, F, G, H, I, J> append(other: Septuple<D, E, F, G, H, I, J>): AtMostDecuple
  }

  interface AtMostQuadruple : AtMostQuintuple {
    override fun <T> append(`object`: T): AtMostQuintuple
    override fun <E, F> append(other: Couple<E, F>): AtMostSextuple
    override fun <E, F, G> append(other: Triple<E, F, G>): AtMostSeptuple
    override fun <E, F, G, H> append(other: Quadruple<E, F, G, H>): AtMostOctuple
    override fun <E, F, G, H, I> append(other: Quintuple<E, F, G, H, I>): AtMostNonuple
    fun <E, F, G, H, I, J> append(other: Sextuple<E, F, G, H, I, J>): AtMostDecuple
  }

  interface AtMostQuintuple : AtMostSextuple {
    override fun <T> append(`object`: T): AtMostSextuple
    override fun <F, G> append(other: Couple<F, G>): AtMostSeptuple
    override fun <F, G, H> append(other: Triple<F, G, H>): AtMostOctuple
    override fun <F, G, H, I> append(other: Quadruple<F, G, H, I>): AtMostNonuple
    fun <F, G, H, I, J> append(other: Quintuple<F, G, H, I, J>): AtMostDecuple
  }

  interface AtMostSextuple : AtMostSeptuple {
    override fun <T> append(`object`: T): AtMostSeptuple
    override fun <G, H> append(other: Couple<G, H>): AtMostOctuple
    override fun <G, H, I> append(other: Triple<G, H, I>): AtMostNonuple
    fun <G, H, I, J> append(other: Quadruple<G, H, I, J>): AtMostDecuple
  }

  interface AtMostSeptuple : AtMostOctuple {
    override fun <T> append(`object`: T): AtMostOctuple
    override fun <H, I> append(other: Couple<H, I>): AtMostNonuple
    fun <H, I, J> append(other: Triple<H, I, J>): AtMostDecuple
  }

  interface AtMostOctuple : AtMostNonuple {
    override fun <T> append(`object`: T): AtMostNonuple
    fun <I, J> append(other: Couple<I, J>): AtMostDecuple
  }

  interface AtMostNonuple : AtMostDecuple {
    fun <T> append(`object`: T): AtMostDecuple
  }

  interface AtMostDecuple
  interface AtMostCouplet<T> : AtMostTriplet<T>, AtMostCouple {
    override fun concat(`object`: T): AtMostTriplet<T>
    override fun concat(other: Couple<T, T>): AtMostQuadruplet<T>
    override fun concat(other: Triple<T, T, T>): AtMostQuintuplet<T>
    override fun concat(other: Quadruple<T, T, T, T>): AtMostSextuplet<T>
    override fun concat(other: Quintuple<T, T, T, T, T>): AtMostSeptuplet<T>
    override fun concat(other: Sextuple<T, T, T, T, T, T>): AtMostOctuplet<T>
    override fun concat(other: Septuple<T, T, T, T, T, T, T>): AtMostNonuplet<T>
    fun concat(other: Octuple<T, T, T, T, T, T, T, T>): AtMostDecuplet<T>
  }

  interface AtMostTriplet<T> : AtMostQuadruplet<T>, AtMostTriple {
    override fun concat(`object`: T): AtMostQuadruplet<T>
    override fun concat(other: Couple<T, T>): AtMostQuintuplet<T>
    override fun concat(other: Triple<T, T, T>): AtMostSextuplet<T>
    override fun concat(other: Quadruple<T, T, T, T>): AtMostSeptuplet<T>
    override fun concat(other: Quintuple<T, T, T, T, T>): AtMostOctuplet<T>
    override fun concat(other: Sextuple<T, T, T, T, T, T>): AtMostNonuplet<T>
    fun concat(other: Septuple<T, T, T, T, T, T, T>): AtMostDecuplet<T>
  }

  interface AtMostQuadruplet<T> : AtMostQuintuplet<T>, AtMostQuadruple {
    override fun concat(`object`: T): AtMostQuintuplet<T>
    override fun concat(other: Couple<T, T>): AtMostSextuplet<T>
    override fun concat(other: Triple<T, T, T>): AtMostSeptuplet<T>
    override fun concat(other: Quadruple<T, T, T, T>): AtMostOctuplet<T>
    override fun concat(other: Quintuple<T, T, T, T, T>): AtMostNonuplet<T>
    fun concat(other: Sextuple<T, T, T, T, T, T>): AtMostDecuplet<T>
  }

  interface AtMostQuintuplet<T> : AtMostSextuplet<T>, AtMostQuintuple {
    override fun concat(`object`: T): AtMostSextuplet<T>
    override fun concat(other: Couple<T, T>): AtMostSeptuplet<T>
    override fun concat(other: Triple<T, T, T>): AtMostOctuplet<T>
    override fun concat(other: Quadruple<T, T, T, T>): AtMostNonuplet<T>
    fun concat(other: Quintuple<T, T, T, T, T>): AtMostDecuplet<T>
  }

  interface AtMostSextuplet<T> : AtMostSeptuplet<T>, AtMostSextuple {
    override fun concat(`object`: T): AtMostSeptuplet<T>
    override fun concat(other: Couple<T, T>): AtMostOctuplet<T>
    override fun concat(other: Triple<T, T, T>): AtMostNonuplet<T>
    fun concat(other: Quadruple<T, T, T, T>): AtMostDecuplet<T>
  }

  interface AtMostSeptuplet<T> : AtMostOctuplet<T>, AtMostSeptuple {
    override fun concat(`object`: T): AtMostOctuplet<T>
    override fun concat(other: Couple<T, T>): AtMostNonuplet<T>
    fun concat(other: Triple<T, T, T>): AtMostDecuplet<T>
  }

  interface AtMostOctuplet<T> : AtMostNonuplet<T>, AtMostOctuple {
    override fun concat(`object`: T): AtMostNonuplet<T>
    fun concat(other: Couple<T, T>): AtMostDecuplet<T>
  }

  interface AtMostNonuplet<T> : AtMostDecuplet<T>, AtMostNonuple {
    fun concat(`object`: T): AtMostDecuplet<T>
  }

  interface AtMostDecuplet<T> : AtMostDecuple, Tuplet<T>
}