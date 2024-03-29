package omnia.data.structure.tuple



class Tuples {
  interface AtLeastCouple<A, B> : Tuple {
    val first: A
    val second: B
    fun <R> mapFirst(mapper: (A) -> R): AtLeastCouple<R, B>
    fun <R> mapSecond(mapper: (B) -> R): AtLeastCouple<A, R>
  }

  interface AtLeastTriple<A, B, C> : AtLeastCouple<A, B> {
    val third: C
    override fun <R> mapFirst(mapper: (A) -> R): AtLeastTriple<R, B, C>
    override fun <R> mapSecond(mapper: (B) -> R): AtLeastTriple<A, R, C>
    fun <R> mapThird(mapper: (C) -> R): AtLeastTriple<A, B, R>
    fun dropFirst(): AtLeastCouple<B, C>
    fun dropSecond(): AtLeastCouple<A, C>
    fun dropThird(): AtLeastCouple<A, B>
  }

  interface AtLeastQuadruple<A, B, C, D> : AtLeastTriple<A, B, C> {
    val fourth: D
    override fun <R> mapFirst(mapper: (A) -> R): AtLeastQuadruple<R, B, C, D>
    override fun <R> mapSecond(mapper: (B) -> R): AtLeastQuadruple<A, R, C, D>
    override fun <R> mapThird(mapper: (C) -> R): AtLeastQuadruple<A, B, R, D>
    fun <R> mapFourth(mapper: (D) -> R): AtLeastQuadruple<A, B, C, R>
    override fun dropFirst(): AtLeastTriple<B, C, D>
    override fun dropSecond(): AtLeastTriple<A, C, D>
    override fun dropThird(): AtLeastTriple<A, B, D>
    fun dropFourth(): AtLeastTriple<A, B, C>
  }

  interface AtLeastQuintuple<A, B, C, D, E> : AtLeastQuadruple<A, B, C, D> {
    val fifth: E
    override fun <R> mapFirst(mapper: (A) -> R): AtLeastQuintuple<R, B, C, D, E>
    override fun <R> mapSecond(mapper: (B) -> R): AtLeastQuintuple<A, R, C, D, E>
    override fun <R> mapThird(mapper: (C) -> R): AtLeastQuintuple<A, B, R, D, E>
    override fun <R> mapFourth(mapper: (D) -> R): AtLeastQuintuple<A, B, C, R, E>
    fun <R> mapFifth(mapper: (E) -> R): AtLeastQuintuple<A, B, C, D, R>
    override fun dropFirst(): AtLeastQuadruple<B, C, D, E>
    override fun dropSecond(): AtLeastQuadruple<A, C, D, E>
    override fun dropThird(): AtLeastQuadruple<A, B, D, E>
    override fun dropFourth(): AtLeastQuadruple<A, B, C, E>
    fun dropFifth(): AtLeastQuadruple<A, B, C, D>
  }

  interface AtLeastSextuple<A, B, C, D, E, F> : AtLeastQuintuple<A, B, C, D, E> {
    val sixth: F
    override fun <R> mapFirst(mapper: (A) -> R): AtLeastSextuple<R, B, C, D, E, F>
    override fun <R> mapSecond(mapper: (B) -> R): AtLeastSextuple<A, R, C, D, E, F>
    override fun <R> mapThird(mapper: (C) -> R): AtLeastSextuple<A, B, R, D, E, F>
    override fun <R> mapFourth(mapper: (D) -> R): AtLeastSextuple<A, B, C, R, E, F>
    override fun <R> mapFifth(mapper: (E) -> R): AtLeastSextuple<A, B, C, D, R, F>
    fun <R> mapSixth(mapper: (F) -> R): AtLeastSextuple<A, B, C, D, E, R>
    override fun dropFirst(): AtLeastQuintuple<B, C, D, E, F>
    override fun dropSecond(): AtLeastQuintuple<A, C, D, E, F>
    override fun dropThird(): AtLeastQuintuple<A, B, D, E, F>
    override fun dropFourth(): AtLeastQuintuple<A, B, C, E, F>
    override fun dropFifth(): AtLeastQuintuple<A, B, C, D, F>
    fun dropSixth(): AtLeastQuintuple<A, B, C, D, E>
  }

  interface AtLeastSeptuple<A, B, C, D, E, F, G> : AtLeastSextuple<A, B, C, D, E, F> {
    val seventh: G
    override fun <R> mapFirst(mapper: (A) -> R): AtLeastSeptuple<R, B, C, D, E, F, G>
    override fun <R> mapSecond(mapper: (B) -> R): AtLeastSeptuple<A, R, C, D, E, F, G>
    override fun <R> mapThird(mapper: (C) -> R): AtLeastSeptuple<A, B, R, D, E, F, G>
    override fun <R> mapFourth(mapper: (D) -> R): AtLeastSeptuple<A, B, C, R, E, F, G>
    override fun <R> mapFifth(mapper: (E) -> R): AtLeastSeptuple<A, B, C, D, R, F, G>
    override fun <R> mapSixth(mapper: (F) -> R): AtLeastSeptuple<A, B, C, D, E, R, G>
    fun <R> mapSeventh(mapper: (G) -> R): AtLeastSeptuple<A, B, C, D, E, F, R>
    override fun dropFirst(): AtLeastSextuple<B, C, D, E, F, G>
    override fun dropSecond(): AtLeastSextuple<A, C, D, E, F, G>
    override fun dropThird(): AtLeastSextuple<A, B, D, E, F, G>
    override fun dropFourth(): AtLeastSextuple<A, B, C, E, F, G>
    override fun dropFifth(): AtLeastSextuple<A, B, C, D, F, G>
    override fun dropSixth(): AtLeastSextuple<A, B, C, D, E, G>
    fun dropSeventh(): AtLeastSextuple<A, B, C, D, E, F>
  }

  interface AtLeastOctuple<A, B, C, D, E, F, G, H> : AtLeastSeptuple<A, B, C, D, E, F, G> {
    val eighth: H
    override fun <R> mapFirst(mapper: (A) -> R): AtLeastOctuple<R, B, C, D, E, F, G, H>
    override fun <R> mapSecond(mapper: (B) -> R): AtLeastOctuple<A, R, C, D, E, F, G, H>
    override fun <R> mapThird(mapper: (C) -> R): AtLeastOctuple<A, B, R, D, E, F, G, H>
    override fun <R> mapFourth(mapper: (D) -> R): AtLeastOctuple<A, B, C, R, E, F, G, H>
    override fun <R> mapFifth(mapper: (E) -> R): AtLeastOctuple<A, B, C, D, R, F, G, H>
    override fun <R> mapSixth(mapper: (F) -> R): AtLeastOctuple<A, B, C, D, E, R, G, H>
    override fun <R> mapSeventh(mapper: (G) -> R): AtLeastOctuple<A, B, C, D, E, F, R, H>
    fun <R> mapEighth(mapper: (H) -> R): AtLeastOctuple<A, B, C, D, E, F, G, R>
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
    val ninth: I
    override fun <R> mapFirst(mapper: (A) -> R): AtLeastNonuple<R, B, C, D, E, F, G, H, I>
    override fun <R> mapSecond(mapper: (B) -> R): AtLeastNonuple<A, R, C, D, E, F, G, H, I>
    override fun <R> mapThird(mapper: (C) -> R): AtLeastNonuple<A, B, R, D, E, F, G, H, I>
    override fun <R> mapFourth(mapper: (D) -> R): AtLeastNonuple<A, B, C, R, E, F, G, H, I>
    override fun <R> mapFifth(mapper: (E) -> R): AtLeastNonuple<A, B, C, D, R, F, G, H, I>
    override fun <R> mapSixth(mapper: (F) -> R): AtLeastNonuple<A, B, C, D, E, R, G, H, I>
    override fun <R> mapSeventh(mapper: (G) -> R): AtLeastNonuple<A, B, C, D, E, F, R, H, I>
    override fun <R> mapEighth(mapper: (H) -> R): AtLeastNonuple<A, B, C, D, E, F, G, R, I>
    fun <R> mapNinth(mapper: (I) -> R): AtLeastNonuple<A, B, C, D, E, F, G, H, R>
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

  interface AtLeastDecuple<A, B, C, D, E, F, G, H, I, J> :
      AtLeastNonuple<A, B, C, D, E, F, G, H, I> {
    val tenth: J
    override fun <R> mapFirst(mapper: (A) -> R): AtLeastDecuple<R, B, C, D, E, F, G, H, I, J>
    override fun <R> mapSecond(mapper: (B) -> R): AtLeastDecuple<A, R, C, D, E, F, G, H, I, J>
    override fun <R> mapThird(mapper: (C) -> R): AtLeastDecuple<A, B, R, D, E, F, G, H, I, J>
    override fun <R> mapFourth(mapper: (D) -> R): AtLeastDecuple<A, B, C, R, E, F, G, H, I, J>
    override fun <R> mapFifth(mapper: (E) -> R): AtLeastDecuple<A, B, C, D, R, F, G, H, I, J>
    override fun <R> mapSixth(mapper: (F) -> R): AtLeastDecuple<A, B, C, D, E, R, G, H, I, J>
    override fun <R> mapSeventh(mapper: (G) -> R): AtLeastDecuple<A, B, C, D, E, F, R, H, I, J>
    override fun <R> mapEighth(mapper: (H) -> R): AtLeastDecuple<A, B, C, D, E, F, G, R, I, J>
    override fun <R> mapNinth(mapper: (I) -> R): AtLeastDecuple<A, B, C, D, E, F, G, H, R, J>
    fun <R> mapTenth(mapper: (J) -> R): AtLeastDecuple<A, B, C, D, E, F, G, H, I, R>
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
    override fun <T> append(item: T): AtMostTriple
    override fun <C, D> append(other: Couple<C, D>): AtMostQuadruple
    override fun <C, D, E> append(other: Triple<C, D, E>): AtMostQuintuple
    override fun <C, D, E, F> append(other: Quadruple<C, D, E, F>): AtMostSextuple
    override fun <C, D, E, F, G> append(other: Quintuple<C, D, E, F, G>): AtMostSeptuple
    override fun <C, D, E, F, G, H> append(other: Sextuple<C, D, E, F, G, H>): AtMostOctuple
    override fun <C, D, E, F, G, H, I> append(other: Septuple<C, D, E, F, G, H, I>): AtMostNonuple
    fun <C, D, E, F, G, H, I, J> append(other: Octuple<C, D, E, F, G, H, I, J>): AtMostDecuple
  }

  interface AtMostTriple : AtMostQuadruple {
    override fun <T> append(item: T): AtMostQuadruple
    override fun <D, E> append(other: Couple<D, E>): AtMostQuintuple
    override fun <D, E, F> append(other: Triple<D, E, F>): AtMostSextuple
    override fun <D, E, F, G> append(other: Quadruple<D, E, F, G>): AtMostSeptuple
    override fun <D, E, F, G, H> append(other: Quintuple<D, E, F, G, H>): AtMostOctuple
    override fun <D, E, F, G, H, I> append(other: Sextuple<D, E, F, G, H, I>): AtMostNonuple
    fun <D, E, F, G, H, I, J> append(other: Septuple<D, E, F, G, H, I, J>): AtMostDecuple
  }

  interface AtMostQuadruple : AtMostQuintuple {
    override fun <T> append(item: T): AtMostQuintuple
    override fun <E, F> append(other: Couple<E, F>): AtMostSextuple
    override fun <E, F, G> append(other: Triple<E, F, G>): AtMostSeptuple
    override fun <E, F, G, H> append(other: Quadruple<E, F, G, H>): AtMostOctuple
    override fun <E, F, G, H, I> append(other: Quintuple<E, F, G, H, I>): AtMostNonuple
    fun <E, F, G, H, I, J> append(other: Sextuple<E, F, G, H, I, J>): AtMostDecuple
  }

  interface AtMostQuintuple : AtMostSextuple {
    override fun <T> append(item: T): AtMostSextuple
    override fun <F, G> append(other: Couple<F, G>): AtMostSeptuple
    override fun <F, G, H> append(other: Triple<F, G, H>): AtMostOctuple
    override fun <F, G, H, I> append(other: Quadruple<F, G, H, I>): AtMostNonuple
    fun <F, G, H, I, J> append(other: Quintuple<F, G, H, I, J>): AtMostDecuple
  }

  interface AtMostSextuple : AtMostSeptuple {
    override fun <T> append(item: T): AtMostSeptuple
    override fun <G, H> append(other: Couple<G, H>): AtMostOctuple
    override fun <G, H, I> append(other: Triple<G, H, I>): AtMostNonuple
    fun <G, H, I, J> append(other: Quadruple<G, H, I, J>): AtMostDecuple
  }

  interface AtMostSeptuple : AtMostOctuple {
    override fun <T> append(item: T): AtMostOctuple
    override fun <H, I> append(other: Couple<H, I>): AtMostNonuple
    fun <H, I, J> append(other: Triple<H, I, J>): AtMostDecuple
  }

  interface AtMostOctuple : AtMostNonuple {
    override fun <T> append(item: T): AtMostNonuple
    fun <I, J> append(other: Couple<I, J>): AtMostDecuple
  }

  interface AtMostNonuple : AtMostDecuple {
    fun <T> append(item: T): AtMostDecuple
  }

  interface AtMostDecuple

  interface AtMostCouplet<T : Any> : AtMostTriplet<T>, AtMostCouple {
    override fun concat(item: T): AtMostTriplet<T>
    override fun concat(other: Couple<T, T>): AtMostQuadruplet<T>
    override fun concat(other: Triple<T, T, T>): AtMostQuintuplet<T>
    override fun concat(other: Quadruple<T, T, T, T>): AtMostSextuplet<T>
    override fun concat(other: Quintuple<T, T, T, T, T>): AtMostSeptuplet<T>
    override fun concat(other: Sextuple<T, T, T, T, T, T>): AtMostOctuplet<T>
    override fun concat(other: Septuple<T, T, T, T, T, T, T>): AtMostNonuplet<T>
    fun concat(other: Octuple<T, T, T, T, T, T, T, T>): AtMostDecuplet<T>
  }

  interface AtMostTriplet<T : Any> : AtMostQuadruplet<T>, AtMostTriple {
    override fun concat(item: T): AtMostQuadruplet<T>
    override fun concat(other: Couple<T, T>): AtMostQuintuplet<T>
    override fun concat(other: Triple<T, T, T>): AtMostSextuplet<T>
    override fun concat(other: Quadruple<T, T, T, T>): AtMostSeptuplet<T>
    override fun concat(other: Quintuple<T, T, T, T, T>): AtMostOctuplet<T>
    override fun concat(other: Sextuple<T, T, T, T, T, T>): AtMostNonuplet<T>
    fun concat(other: Septuple<T, T, T, T, T, T, T>): AtMostDecuplet<T>
  }

  interface AtMostQuadruplet<T : Any> : AtMostQuintuplet<T>, AtMostQuadruple {
    override fun concat(item: T): AtMostQuintuplet<T>
    override fun concat(other: Couple<T, T>): AtMostSextuplet<T>
    override fun concat(other: Triple<T, T, T>): AtMostSeptuplet<T>
    override fun concat(other: Quadruple<T, T, T, T>): AtMostOctuplet<T>
    override fun concat(other: Quintuple<T, T, T, T, T>): AtMostNonuplet<T>
    fun concat(other: Sextuple<T, T, T, T, T, T>): AtMostDecuplet<T>
  }

  interface AtMostQuintuplet<T : Any> : AtMostSextuplet<T>, AtMostQuintuple {
    override fun concat(item: T): AtMostSextuplet<T>
    override fun concat(other: Couple<T, T>): AtMostSeptuplet<T>
    override fun concat(other: Triple<T, T, T>): AtMostOctuplet<T>
    override fun concat(other: Quadruple<T, T, T, T>): AtMostNonuplet<T>
    fun concat(other: Quintuple<T, T, T, T, T>): AtMostDecuplet<T>
  }

  interface AtMostSextuplet<T : Any> : AtMostSeptuplet<T>, AtMostSextuple {
    override fun concat(item: T): AtMostSeptuplet<T>
    override fun concat(other: Couple<T, T>): AtMostOctuplet<T>
    override fun concat(other: Triple<T, T, T>): AtMostNonuplet<T>
    fun concat(other: Quadruple<T, T, T, T>): AtMostDecuplet<T>
  }

  interface AtMostSeptuplet<T : Any> : AtMostOctuplet<T>, AtMostSeptuple {
    override fun concat(item: T): AtMostOctuplet<T>
    override fun concat(other: Couple<T, T>): AtMostNonuplet<T>
    fun concat(other: Triple<T, T, T>): AtMostDecuplet<T>
  }

  interface AtMostOctuplet<T : Any> : AtMostNonuplet<T>, AtMostOctuple {
    override fun concat(item: T): AtMostNonuplet<T>
    fun concat(other: Couple<T, T>): AtMostDecuplet<T>
  }

  interface AtMostNonuplet<T : Any> : AtMostDecuplet<T>, AtMostNonuple {
    fun concat(item: T): AtMostDecuplet<T>
  }

  interface AtMostDecuplet<T : Any> : AtMostDecuple, Tuplet<T>
}